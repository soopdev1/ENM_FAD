<%-- 
    Document   : index
    Created on : 25 gen 2024, 12:33:40
    Author     : Salvatore
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <header
            class="it-header-wrapper it-header-sticky"
            data-bs-toggle="sticky"
            data-bs-position-type="fixed"
            data-bs-sticky-class-name="is-sticky"
            data-bs-target="#header-nav-wrapper"
            >
            

            <div class="it-nav-wrapper">
                <div class="it-header-center-wrapper">
                    <div class="container">
                        <div class="row">
                            <div class="col-12">
                                <div class="it-header-center-content-wrapper">
                                    <div class="it-brand-wrapper">
                                        <a href="">
                                            <svg class="icon" aria-hidden="true">
                                            <use href=""></use>
                                            </svg>
                                            <div class="it-brand-text">
                                                <div class="it-brand-title"><h3 class="kt-login__title kt-font-io" style="font-size:2rem; color:white !important;"><b>YES I STARTUP - Regione Toscana</b></h3></div>
                                                <!--<div class="it-brand-tagline d-none d-md-block">Uno dei tanti Comuni d'Italia</div>-->
                                            </div>
                                        </a>
                                    </div>
                                    <div class="it-right-zone">
                                        <!--<div class="it-socials d-none d-md-flex">
                                            <span>Seguici su</span>
                                            <ul>
                                                <li>
                                                    <a href="#" aria-label="Facebook" target="_blank">
                                                        <svg class="icon">
                                                        <use href="/dist/svg/sprites.svg#it-facebook"></use>
                                                        </svg>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#" aria-label="Github" target="_blank">
                                                        <svg class="icon">
                                                        <use href="/dist/svg/sprites.svg#it-github"></use>
                                                        </svg>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#" aria-label="Twitter" target="_blank">
                                                        <svg class="icon">
                                                        <use href="/dist/svg/sprites.svg#it-twitter"></use>
                                                        </svg>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>-->
                                        <!--<div class="it-search-wrapper">
                                            <span class="d-none d-md-block">Cerca</span>
                                            <a class="search-link rounded-icon" aria-label="Cerca nel sito" href="#">
                                                <svg class="icon">
                                                <use href="/dist/svg/sprites.svg#it-search"></use>
                                                </svg>
                                            </a>
                                        </div>-->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="it-header-navbar-wrapper" id="header-nav-wrapper">
                    <div class="container">
                        <div class="row">
                            <div class="col-12">
                                <!--start nav-->
                                <nav class="navbar navbar-expand-lg has-megamenu" aria-label="Navigazione principale">
                                    <button
                                        class="custom-navbar-toggler"
                                        type="button"
                                        aria-controls="nav4"
                                        aria-expanded="false"
                                        aria-label="Mostra/Nascondi la navigazione"
                                        data-bs-target="#nav4"
                                        data-bs-toggle="navbarcollapsible"
                                        >
                                        <svg class="icon">
                                        <use href=""></use>
                                        </svg>
                                    </button>
                                    <div class="navbar-collapsable" id="nav4" style="display: none">
                                        <div class="overlay" style="display: none"></div>
                                        <div class="close-div">
                                            <button class="btn close-menu" type="button">
                                                <span class="visually-hidden">Nascondi la navigazione</span>
                                                <svg class="icon">
                                                <use href=""></use>
                                                </svg>
                                            </button>
                                        </div>
                                        <!--<div class="menu-wrapper">
                                            <ul class="navbar-nav">
                                                <li class="nav-item ">
                                                    <a
                                                        class="nav-link "
                                                        href="/bootstrap-italia/docs/esempi/comuni/template-amministrazione/"
                                                        >
                                                        <span>Amministrazione</span>

                                                    </a>
                                                </li>
                                                <li class="nav-item ">
                                                    <a class="nav-link " href="/bootstrap-italia/docs/esempi/comuni/template-novita/">
                                                        <span>Novità</span>

                                                    </a>
                                                </li>
                                                <li class="nav-item ">
                                                    <a class="nav-link " href="/bootstrap-italia/docs/esempi/comuni/template-servizi/">
                                                        <span>Servizi</span>

                                                    </a>
                                                </li>
                                                <li class="nav-item ">
                                                    <a class="nav-link " href="/bootstrap-italia/docs/esempi/comuni/template-documenti/">
                                                        <span>Documenti e Dati</span>

                                                    </a>
                                                </li>
                                            </ul>
                                            <ul class="navbar-nav navbar-secondary">
                                                <li class="nav-item">
                                                    <a class="nav-link" href="/bootstrap-italia/docs/esempi/comuni/template-argomenti-argomento/"> Argomento 1</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" href="#"> Argomento 2</a>
                                                </li>
                                                <li class="nav-item ">
                                                    <a class="nav-link " href="/bootstrap-italia/docs/esempi/comuni/template-argomenti">
                                                        <span class="fw-bold">Tutti gli argomenti...</span>

                                                    </a>
                                                </li>
                                            </ul>
                                        </div>-->
                                    </div>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>
    </body>
</html>
