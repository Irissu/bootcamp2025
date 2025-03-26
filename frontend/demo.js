// PRUEBAS
let txt = document.querySelector('#txtNombre');
let btn = document.querySelector('input[type="button"]');
let saludar = function() {
    document.getElementById('outSaludo').textContent = `Hola ${txt.value}`;
}
let registro = function() {
    console.log(`Hola ${txt.value}`);
}
// btn.onClick = saludar;
btn.addEventListener('click', saludar);
btn.addEventListener('click', registro);
document.querySelector('input[value="Quitar"]').addEventListener('click', function() {
    btn.removeEventListener('click', registro);
});