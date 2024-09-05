// Constantes

const csvForm = document.getElementById("csvForm");
const excelForm = document.getElementById("excelForm");

// Habilitar todos los popovers
document.addEventListener('DOMContentLoaded', function() {
	// Inicializar todos los popovers en la página
	const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
	const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl));
});

// Mostrar y ocultar los formularios según la opción seleccionada en el dropdown o al precionar el boton "close"
// Item de CSV
/*document.getElementById("importCsv").addEventListener("click", function(event) {
	event.preventDefault();
	csvForm.style.display = "flex";
	excelForm.style.display = "none";
});

// Item de excel
document.getElementById("importExcel").addEventListener("click", function(event) {
	event.preventDefault();

	csvForm.style.display = "none";
	excelForm.style.display = "flex";
});

// botones "close" de formularios de importacion
document.getElementById("btn-csv-form-close").addEventListener("click", function() {
	csvForm.style.display = "none";
});

document.getElementById("btn-excel-form-close").addEventListener("click", function(){
	excelForm.style.display = "none";
})*/

