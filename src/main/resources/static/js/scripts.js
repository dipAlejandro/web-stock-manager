// Constantes
const csvForm = document.getElementById("csvForm");
const excelForm = document.getElementById("excelForm");

// Habilitar todos los popovers
document.addEventListener('DOMContentLoaded', function () {
    // Inicializar todos los popovers en la página
    const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
    const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl));
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
});

function setEntityIdToDelete(EntityId, elementId) {
    //console.log("Setting product ID to delete:", id); // Debugging
    const hiddenInputId = document.getElementById(elementId);
    hiddenInputId.value = EntityId;
  }

