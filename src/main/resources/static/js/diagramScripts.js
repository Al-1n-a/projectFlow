// Глобальные переменные
let currentDiagramId = null;

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    initAddDiagramButton();
    initSaveDiagramButton();
    initDiagramsDragAndDrop();
});

// Показать ошибку
function showError(message) {
    const errorElement = document.createElement('div');
    errorElement.className = 'alert alert-danger position-fixed top-0 start-50 translate-middle-x mt-3';
    errorElement.style.zIndex = '1100';
    errorElement.textContent = message;
    document.body.prepend(errorElement);
    setTimeout(() => errorElement.remove(), 5000);
}

// Показать загрузку
function showLoading() {
    document.getElementById('loadingOverlay').style.display = 'block';
}

// Скрыть загрузку
function hideLoading() {
    document.getElementById('loadingOverlay').style.display = 'none';
}

// Инициализация кнопки добавления диаграммы
function initAddDiagramButton() {
    document.getElementById('addDiagramBtn').addEventListener('click', async function() {
        const diagramType = document.getElementById('diagramTypeSelect').value;
        try {
            showLoading();
            const diagram = await addDiagram(diagramType);
            createDiagramElement(diagram);
        } catch (error) {
            showError("Failed to add diagram: " + error.message);
            console.error("Error adding diagram:", error);
        } finally {
            hideLoading();
        }
    });
}

// Инициализация кнопки сохранения диаграммы
function initSaveDiagramButton() {
    document.getElementById('saveDiagramBtn').addEventListener('click', async function() {
        try {
            showLoading();
            const diagramId = document.getElementById('editDiagramId').value;
            const updateData = getDiagramUpdateData();
            const updatedDiagram = await updateDiagram(diagramId, updateData);
            updateDiagramElement(updatedDiagram);
            bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
        } catch (error) {
            showError("Failed to update diagram: " + error.message);
            console.error("Error updating diagram:", error);
        } finally {
            hideLoading();
        }
    });
}

// Инициализация перетаскивания диаграмм
function initDiagramsDragAndDrop() {
    document.querySelectorAll('.diagram').forEach(diagramEl => {
        makeDraggable(diagramEl);
    });
}

// Добавление новой диаграммы
async function addDiagram(diagramType) {
    const boardId = document.querySelector('meta[name="boardId"]').content;
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    if (!boardId) {
        showError("Cannot add diagram: board not loaded");
        return;
    }

    try {
        const response = await fetch(`/boards/${boardId}/diagrams`, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken,
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({ type: diagramType, boardId: boardId })
        });

        if (!response.ok) {
            throw new Error(await response.text() || `HTTP ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Error adding diagram:", error);
        throw error;
    }
}

// Обновление диаграммы
async function updateDiagram(diagramId, updateData) {
    const boardId = document.querySelector('meta[name="boardId"]').content;
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    const response = await fetch(`/boards/${boardId}/diagrams/${diagramId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(updateData)
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
}

// Удаление диаграммы
async function deleteDiagram(diagramId) {
    if (!confirm('Вы уверены, что хотите удалить диаграмму?')) return;

    const boardId = document.querySelector('meta[name="boardId"]').content;
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    try {
        showLoading();
        console.log("Sending delete request for diagram", diagramId);

        const response = await fetch(`/boards/${boardId}/diagrams/${diagramId}`, {
            method: 'DELETE',
            headers: {
                [csrfHeader]: csrfToken,
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        });

        console.log("Delete response status:", response.status);

        if (!response.ok) {
            const errorText = await response.text();
            console.error("Delete failed with response:", errorText);
            throw new Error(errorText || `HTTP ${response.status}`);
        }

        console.log("Removing diagram from DOM");
        document.querySelector(`.diagram[data-id="${diagramId}"]`)?.remove();
    } catch (error) {
        console.error("Full delete error:", error);
        showError("Ошибка удаления: " + (error.message || "неизвестная ошибка"));
    } finally {
        hideLoading();
    }
}

// Создание элемента диаграммы
function createDiagramElement(diagram) {
    const boardCanvas = document.getElementById('boardCanvas');
    const diagramEl = document.createElement('div');
    diagramEl.className = 'diagram';
    diagramEl.dataset.id = diagram.id;
    diagramEl.style.left = `${diagram.positionX}px`;
    diagramEl.style.top = `${diagram.positionY}px`;
    diagramEl.style.width = `${diagram.width}px`;
    diagramEl.style.height = `${diagram.height}px`;

    diagramEl.innerHTML = `
        <div class="diagram-header">
            <span>
                <i class="fas ${getDiagramIconClass(diagram.type)}"></i>
                <span>${diagram.title || 'Unnamed Diagram'}</span>
            </span>
            <div class="diagram-actions">
                <button class="diagram-action-btn" onclick="showEditModal(${diagram.id})">
                    <i class="fas fa-cog"></i>
                </button>
                <button class="diagram-action-btn" onclick="deleteDiagram(${diagram.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
        <div class="diagram-content">
            <div class="text-muted">
                <i class="fas ${getDiagramIconClass(diagram.type)}"></i>
                ${getDiagramTypeName(diagram.type)}
            </div>
        </div>
    `;

    boardCanvas.appendChild(diagramEl);
    makeDraggable(diagramEl);
}

// Обновление элемента диаграммы
function updateDiagramElement(diagram) {
    const diagramEl = document.querySelector(`.diagram[data-id="${diagram.id}"]`);
    if (!diagramEl) return;

    diagramEl.style.left = `${diagram.positionX}px`;
    diagramEl.style.top = `${diagram.positionY}px`;
    diagramEl.style.width = `${diagram.width}px`;
    diagramEl.style.height = `${diagram.height}px`;

    const header = diagramEl.querySelector('.diagram-header span span');
    if (header) header.textContent = diagram.title || 'Unnamed Diagram';
}

// Показать модальное окно редактирования
function showEditModal(diagramId, diagramType) {
    const boardId = document.querySelector('meta[name="boardId"]').content;

    fetch(`/boards/${boardId}/diagrams/${diagramId}`)
        .then(response => {
            if (!response.ok) throw new Error('Failed to load diagram');
            return response.json();
        })
        .then(diagram => {
            // Общие поля для всех диаграмм
            document.getElementById('editDiagramId').value = diagram.id;
            document.getElementById('editTitle').value = diagram.title;

            // Специфичные поля для каждого типа
            const modalId = `${diagramType.toLowerCase()}EditModal`;
            const modalElement = document.getElementById(modalId);

            if (modalElement) {
                if (diagramType === 'GANTT') {
                    const config = JSON.parse(diagram.config || '{}');
                    document.getElementById('ganttStartDate').value = config.startDate || '';
                    document.getElementById('ganttEndDate').value = config.endDate || '';
                }
                else if (diagramType === 'DFD') {
                    document.getElementById('dfdLevel').value = diagram.level || 0;
                }
                else if (diagramType === 'WBS') {
                    document.getElementById('wbsProjectCode').value = diagram.projectCode || '';}
                // Добавьте обработку других типов

                new bootstrap.Modal(modalElement).show();
            } else {
                showError(`Edit modal not found for type: ${diagramType}`);
            }
        })
        .catch(error => {
            showError("Failed to load diagram: " + error.message);
            console.error(error);
        });
}

async function saveGanttDiagram() {
    const diagramId = document.getElementById('ganttDiagramId').value;
    const updateData = {
        title: document.getElementById('ganttTitle').value,
        startDate: document.getElementById('ganttStartDate').value,
        endDate: document.getElementById('ganttEndDate').value
    };

    await updateDiagram(diagramId, updateData);
    bootstrap.Modal.getInstance(document.getElementById('ganttEditModal')).hide();
}

async function saveDfdDiagram() {
    const diagramId = document.getElementById('dfdDiagramId').value;
    const updateData = {
        title: document.getElementById('dfdTitle').value,
        level: document.getElementById('dfdLevel').value
    };

    await updateDiagram(diagramId, updateData);
    bootstrap.Modal.getInstance(document.getElementById('dfdEditModal')).hide();
}

async function saveWbsDiagram() {
    const diagramId = document.getElementById('wbsDiagramId').value;
    const updateData = {
        title: document.getElementById('wbsTitle').value,
        level: document.getElementById('wbsProjectCode').value
    };

    await updateDiagram(diagramId, updateData);
    bootstrap.Modal.getInstance(document.getElementById('wbsEditModal')).hide();
}

// Получить данные для обновления из формы
function getDiagramUpdateData() {
    return {
        title: document.getElementById('editTitle').value,
        positionX: parseInt(document.getElementById('editPosX').value),
        positionY: parseInt(document.getElementById('editPosY').value),
        width: parseInt(document.getElementById('editWidth').value),
        height: parseInt(document.getElementById('editHeight').value)
    };
}

// Получить класс иконки для типа диаграммы
function getDiagramIconClass(type) {
    const iconMap = {
        'GANTT': 'fa-tasks',
        'DFD': 'fa-project-diagram',
        'WBS': 'fa-sitemap',
        'SAM': 'fa-users',
        'TIMELINE': 'fa-timeline',
        'CFD': 'fa-chart-line',
        'AGENDA': 'fa-list',
        'ISHIKAWA': 'fa-fish',
        'RT': 'fa-search',
        'STRATEGYMAP': 'fa-map',
        'AGILE': 'fa-columns'
    };
    return iconMap[type] || 'fa-diagram-nested';
}

// Получить название типа диаграммы
function getDiagramTypeName(type) {
    const nameMap = {
        'GANTT': 'Диаграмма Ганта',
        'DFD': 'Диаграмма потока данных',
        'WBS': 'Структура распределения работ',
        'SAM': 'Матрица анализа заинтересованных сторон',
        'TIMELINE': 'Временная шкала проекта',
        'CFD': 'Накопительная диаграмма потоков',
        'AGENDA': 'Повестка дня',
        'ISHIKAWA': 'Диаграмма Исикавы',
        'RT': 'Исследование',
        'STRATEGYMAP': 'Стратегическая карта',
        'AGILE': 'Agile-доска'
    };
    return nameMap[type] || 'Диаграмма';
}

// Настройка перетаскивания
function makeDraggable(element) {
    interact(element)
        .draggable({
            inertia: true,
            modifiers: [
                interact.modifiers.restrictRect({
                    restriction: 'parent',
                    endOnly: true
                })
            ],
            autoScroll: true,
            listeners: {
                move: dragMoveListener,
                end: function(event) {
                    const diagramId = event.target.dataset.id;
                    const x = parseInt(event.target.style.left);
                    const y = parseInt(event.target.style.top);

                    updateDiagram(diagramId, {
                        positionX: x,
                        positionY: y,
                        width: parseInt(event.target.style.width),
                        height: parseInt(event.target.style.height)
                    }).catch(error => {
                        console.error("Error updating diagram position:", error);
                    });
                }
            }
        })
        .resizable({
            edges: { left: true, right: true, bottom: true, top: true },
            listeners: {
                move: function(event) {
                    const target = event.target;
                    target.style.width = event.rect.width + 'px';
                    target.style.height = event.rect.height + 'px';
                },
                end: function(event) {
                    const diagramId = event.target.dataset.id;
                    updateDiagram(diagramId, {
                        width: parseInt(event.target.style.width),
                        height: parseInt(event.target.style.height)
                    }).catch(error => {
                        console.error("Error updating diagram size:", error);
                    });
                }
            }
        });

    function dragMoveListener(event) {
        const target = event.target;
        const x = (parseFloat(target.style.left) || 0) + event.dx;
        const y = (parseFloat(target.style.top) || 0) + event.dy;

        target.style.left = x + 'px';
        target.style.top = y + 'px';
    }
}

// Глобальные функции
window.showEditModal = showEditModal;
window.deleteDiagram = deleteDiagram;