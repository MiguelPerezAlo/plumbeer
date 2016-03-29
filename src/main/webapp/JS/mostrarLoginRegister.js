/**
 * Created by Miguel on 22/03/2016.
 */
$(document).ready(iniciar);

function iniciar(){

    $(".bton").click(moveContainer);
    $("#login").mouseenter(invertirColores);
    $("#login").mouseleave(colorNormal);
    $("#register").mouseenter(invertirColores);
    $("#register").mouseleave(colorNormal);
}

function moveContainer(){

    $(".container").animate({top:"0"}, {duration:200}, {step:mostrarLogin()});
}

function mostrarLogin(){
    $(".forms").css("display","inline-block");
}

function invertirColores(){
    $(this).css("backgroundColor","rgba(255,255,255)");
    $(this).css("color","rgb(44,219,246)");
}

function colorNormal(){
    $(this).css("color","rgb(255,255,255)");
    $(this).css("backgroundColor","rgba(44,219,246,0.2)");
}
