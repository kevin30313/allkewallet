$(document).ready(function () {
    // Usuarios registrados (en memoria)
    const users = [
        { email: 'admin@example.com', password: btoa('key001') },
        // Agrega más usuarios según sea necesario
    ];

    // Muestra el formulario de registro y oculta el de inicio de sesión
    $("#registerLink").click(function () {
        $("#loginContainer").hide();
        $("#registerContainer").show();
    });

    // Muestra el formulario de inicio de sesión y oculta el de registro
    $("#loginLink").click(function () {
        $("#loginContainer").show();
        $("#registerContainer").hide();
    });

    // Acciones para el formulario de inicio de sesión
    $("#loginForm").submit(function (event) {
        event.preventDefault();
        const email = $("#username").val();
        const password = btoa($("#password").val()); // Codifica la contraseña a base64

        // Verifica las credenciales del usuario
        const user = users.find(u => u.email === email && u.password === password);
        if (user) {
            // Acceso permitido
            console.log(`Bienvenido, ${email}!`);
            // Redirige al usuario a loggeduser.html
            window.location.href = "loggedusermenu.html";
        } else {
            // Acceso denegado
            console.log('Credenciales incorrectas. Inténtalo de nuevo.');
        }
    });

    // Acciones para el formulario de registro
    $("#registerForm").submit(function (event) {
        event.preventDefault();
        const email = $("#email").val();
        const newPassword = btoa($("#newPassword").val()); // Codifica la contraseña a base64

        // Verifica si el usuario ya está registrado
        if (users.some(u => u.email === email)) {
            console.log('Este usuario ya está registrado. Inicia sesión en lugar de registrarte.');
        } else {
            // Registra al nuevo usuario
            users.push({ email: email, password: newPassword });
            console.log('Usuario registrado exitosamente.');

            // Oculta el formulario de registro y muestra el de inicio de sesión
            $("#registerContainer").hide();
            $("#loginContainer").show();
        }
    });
});
