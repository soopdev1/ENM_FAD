<%@page import="org.joda.time.DateTime"%>
<%@page import="rc.so.engine.Action"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Login - MCN</title>
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link href="css/googlefontcss.css" rel="stylesheet">
        <link href="css/sb-admin-2.min.css" rel="stylesheet">
        <link rel="shortcut icon" href="favicon.ico" />    
        <%
            String date1 = new DateTime().toString("dd/MM/yyyy");
            String linkedu = Action.get_Path("linkedu1");
            String error = Action.getRequestValue(request, "error");
        %>
        <script type="text/javascript">
            function checkerror() {
                var er1 = '<%=error%>';
                if (er1 === "yes") {
                    document.getElementById('modalerrorbutton').click();
                }

            }
        </script>
    </head>
    <body class="bg-gradient-primary" onload="return checkerror();">
        <div class="container">
            <!-- Button trigger modal -->
            <div class="modal fade"  tabindex="-1" aria-hidden="true">
                <button id="modalerrorbutton" type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
                    Launch demo modal
                </button>
            </div>
            <!-- Modal -->
            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-danger" id="exampleModalLabel">ERRORE</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            I DATI DI ACCESSO INSERITI NON SONO CORRETTI. CONTROLLARE LA MAIL RICEVUTA.
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Outer Row -->
            <div class="row justify-content-center">

                <div class="col-xl-10 col-lg-12 col-md-9">

                    <div class="card o-hidden border-0 shadow-lg my-5">
                        <div class="card-body p-0">
                            <!-- Nested Row within Card Body -->
                            <div class="row">
                                <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
                                <div class="col-lg-6">
                                    <div class="p-5">
                                        <div class="text-center">
                                            <h1 class="h4 text-gray-900 mb-4">Portale FAD <br> YES I START UP TOSCANA</h1>
                                                <%if (Action.test) {%>
                                            <span class="bold text-center text-primary center-block"><img src="images/beta.png" alt="" height="100"/></span>
                                                <%}%>
                                            <hr>
                                            <h2 class="h4 text-gray-900 mb-4">LEZIONE DEL: <%=date1%></h2>
                                        </div>
                                        <form action="" method="POST">
                                            <hr style="background-color: #c82333;">
                                            <small class="form-text text-muted">AREA LAVORO</small>
                                            <small class="form-text text-muted">E' possibile accedere in qualsiasi momento alla piattaforma dedicata all'area lavoro inserendo l'ultima coppia di credenziali ricevuta sulla propria email.</small>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-user"></i></span>
                                                </div>
                                                <input type="text" id="username" name="us1" class="form-control required" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1" required/>
                                            </div>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-key"></i></span>
                                                </div>
                                                <input type="password" id="password" name="ps1"  class="form-control required" placeholder="Password" aria-label="Password" aria-describedby="basic-addon1" required />
                                            </div>
                                            <div class="input-group mb-3">
                                                <button class="btn btn-dark btn-user btn-block" onclick="return sendedubik();" type="button">
                                                    LOGIN AREA LAVORO
                                                </button>
                                            </div>
                                        </form>
                                        <form action="<%=linkedu%>" method="POST" id="formedu_r">
                                            <input type="hidden" name="us_retk" id="RefreshToken_r" value="" />
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>

    </div>

    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.min.js"></script>
    <script type="text/javascript">
                                                    function sendedubik() {
                                                        var username = $("#username").val();
                                                        var password = $("#password").val();
                                                        $.ajax({
                                                            url: "Login",
                                                            async: false,
                                                            type: "POST",
                                                            crossDomain: true,
                                                            data: {"type": "login_edubik", "username": username, "password": password},
                                                            success: function (data, status, xhr) {   // success callback function
                                                                if (data === null || data.startsWith("ERROR")) {
                                                                    document.getElementById("RefreshToken_r").value = "";
                                                                    document.getElementById('modalerrorbutton').click();
                                                                } else {
                                                                    document.getElementById("RefreshToken_r").value = data;
                                                                    document.getElementById('formedu_r').submit();
                                                                }
                                                            },
                                                            error: function (jqXhr, textStatus, errorMessage) { // error callback 
                                                                console.error("Error: " + jqXhr);
                                                                console.error("Error: " + textStatus);
                                                                console.error("Error: " + errorMessage);
                                                            }
                                                        });
                                                    }
    </script>
</body>

</html>
