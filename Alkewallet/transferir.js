// transfer.js

function transferMoney() {
    // Restablece los mensajes de error
    $('.error').text('');

    // Captura los valores del formulario
    var nombreDestinatario = $('#nombreDestinatario').val();
    var rut = $('#rut').val();
    var correo = $('#correo').val();
    var amount = $('#amount').val();

    // Verifica si los campos están completos
    if (nombreDestinatario && rut && correo && amount) {
        // Realiza la solicitud AJAX si los campos están completos
        $.ajax({
            type: 'POST',
            url: 'tu_url_de_backend',
            data: {
                nombreDestinatario: nombreDestinatario,
                rut: rut,
                correo: correo,
                amount: amount
            },
            success: function (response) {
                // Maneja la respuesta del servidor
                console.log(response);
                alert('Transferencia exitosa');
            },
            error: function (error) {
                // Maneja los errores de la solicitud
                console.error(error);
                alert('Error en la transferencia. Inténtalo de nuevo.');
            }
        });
    } else {
        // Muestra mensajes de error si algún campo está vacío
        if (!nombreDestinatario) {
            $('#errorNombreDestinatario').text('Por favor, ingresa el nombre del destinatario.');
        }
        if (!rut) {
            $('#errorRut').text('Por favor, ingresa el RUT.');
        }
        if (!correo) {
            $('#errorCorreo').text('Por favor, ingresa el correo.');
        }
        if (!amount) {
            $('#errorAmount').text('Por favor, ingresa el monto.');
        }

        // Muestra una alerta general
        alert('Por favor, completa todos los campos antes de transferir.');
    }
}
