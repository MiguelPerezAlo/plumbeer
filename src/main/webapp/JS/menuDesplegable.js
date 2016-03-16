/**
 * Created by miguel.perez on 16/03/2016.
 */
window.addEventListener("load", inicio, false);

function inicio(){
    var bmenu = document.getElementById("bmenu");
    bmenu.addEventListener("click", showBubbles, false);
}

function showBubbles(){
    var bubbles = document.getElementsByClassName("card-container");
    bubbles.style.display = "inline-block";
}
