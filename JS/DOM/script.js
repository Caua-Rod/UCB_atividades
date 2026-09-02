let B1 = document.getElementById("B1");
let B2 = document.getElementById("B2");
let N = document.getElementById("N");
let contador = 0;
N.textContent = contador;

B1.onclick = function(){
    contador++
    N.textContent = contador;
}
B2.onclick = function(){
    if(contador<=0){
        alert("Valor ja esta em zero");
    } else{
        contador--
        N.textContent = contador;
    }
}

//Lista
let campo = document.getElementById("campo");
let lista = document.getElementById("lista");
        
campo.onkeydown = function(event){
    if(event.key == "Enter"){
        let novoParagrafo = document.createElement('p');
        novoParagrafo.textContent = campo.value;
        lista.appendChild(novoParagrafo);
        campo.value = "";
    };
}