/**
 * Created by Miguel on 22/03/2016.
 */
$(document).ready(iniciar);

function iniciar(){

    $("#login").click(moveContainer);
}

function moveContainer(){

    $(".container").animate({top:"0"}, {duration:200}, {step:mostrarLogin()});
}

function mostrarLogin(){

    $(".forms").css("display","inline-block");

}
