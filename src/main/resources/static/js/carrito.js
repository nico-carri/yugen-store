function agregarAsync(formElement) {
    const formData = new FormData(formElement);
    
    fetch('/carrito/agregar', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        // redireccion al login
        if (response.redirected && response.url.includes('/usuario/login')) {
            Swal.fire({
                title: '¡Sesión necesaria!',
                text: 'Debes iniciar sesión para comprar.',
                icon: 'info',
                showCancelButton: true,
                confirmButtonText: 'Ir al Login',
                cancelButtonText: 'Seguir mirando'
            }).then((result) => {
                if (result.isConfirmed) window.location.href = '/usuario/login';
            });
            return null; // null para que se corte aca
        }

        // si esta logeado lee el JSON que viene del Controller
        if (response.ok) {
            return response.json();
        }
        throw new Error('Error en la respuesta del servidor');
    })
    .then(data => {
        if (!data) return;

        // muestra alerta y actualiza el badge
        if (data.status === 'success') {
            Swal.fire({ 
                title: '¡Añadido!', 
                text: data.message, 
                icon: 'success', 
                timer: 1000, 
                showConfirmButton: false 
            });

            // actualizar el numerito del carrito
            const badge = document.getElementById('cart-badge');
            if (badge) {
                badge.innerText = data.totalCarrito;
                
                // animacion 
                badge.animate([
                    { transform: 'scale(1)' },
                    { transform: 'scale(1.3)' },
                    { transform: 'scale(1)' }
                ], { duration: 300 });
            }
            
            // actualizar carrito lateral
            if (typeof actualizarMiniCarrito === 'function') {
                actualizarMiniCarrito();
            }
        }
    })
    .catch(error => {
        console.error(error);
    });
}

// Función para sumar/restar
async function cambiarCantidadAsync(id, accion) {
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;

    const params = new URLSearchParams();
    params.append('id', id);
    params.append('accion', accion);

    try {
        // Asumimos que creaste el endpoint /api/actualizar-cantidad en el Controller
        const response = await fetch('/carrito/api/actualizar-cantidad', {
            method: 'POST',
            headers: { 
                [header]: token,
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params
        });

        const data = await response.json();

        if (data.status === 'success') {
            if (data.cantidadIndividual === 0) {
                location.reload(); // Si llega a 0, recargamos para quitar la fila
                return;
            }

            // 1. Actualizar numerito individual del producto
            const elCant = document.getElementById('cant-' + id);
            if (elCant) elCant.innerText = data.cantidadIndividual;

            // 2. Actualizar el precio total en la vista
            const elTotal = document.getElementById('total-precio');
            if (elTotal) elTotal.innerText = '$' + data.nuevoTotalPrecio;

            // 3. Actualizar todos los contadores (Badge y Texto de cabecera)
            actualizarUIContadores(data.totalUnidades);
        }
    } catch (error) {
        console.error('Error al actualizar:', error);
    }
}

// Función auxiliar para mantener la coherencia en toda la pantalla
function actualizarUIContadores(total) {
    // Actualiza el badge del Navbar
    const badge = document.getElementById('cart-badge');
    if (badge) badge.innerText = total;

    // Actualiza el texto "(X productos)" en la cabecera del carrito
    const textoUnidades = document.getElementById('total-unidades-texto');
    if (textoUnidades) {
        textoUnidades.innerText = total;
        
        // Opcional: Cambiar de singular a plural dinámicamente
        const etiquetaProd = document.getElementById('etiqueta-productos');
        if (etiquetaProd) {
            etiquetaProd.innerText = (total === 1) ? ' producto' : ' productos';
        }
    }
}

function actualizarMiniCarrito() {
    const contenedor = document.getElementById('carrito-lateral-body'); 
    if (!contenedor) return;

    fetch('/carrito/resumen') 
        .then(res => res.text())
        .then(html => {
            contenedor.innerHTML = html;
        });
}