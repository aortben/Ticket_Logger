package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración para habilitar la gestión de recursos estáticos en
 * Spring MVC.
 * Permite servir archivos desde un directorio externo utilizando las propiedades
 * definidas en el archivo .env o application.properties.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Logger para registrar eventos importantes
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    // Ruta de subida de archivos, inyectada desde el archivo de propiedades
    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    /**
     * Configura los manejadores de recursos estáticos.
     * Registra un ResourceHandler para servir archivos desde un directorio
     * externo especificado por la variable de entorno UPLOAD_PATH.
     *
     * @param registry el registro de manejadores de recursos
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Verificar si la variable UPLOAD_PATH está configurada
        if (uploadPath != null && !uploadPath.isEmpty()) {
            logger.info("UPLOAD_PATH configurado correctamente: {}", uploadPath);

            // Configurar Spring para servir archivos desde la ruta obtenida
            // Cuando el servidor reciba una solicitud que coincida con /uploads/**,
            // buscará el archivo en la ruta física uploadPath del sistema de archivos del servidor.
            // Se usa el prefijo file:/// para asegurar compatibilidad con rutas absolutas en Windows.
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:///" + uploadPath + "/");

        } else {
            logger.error("La variable de entorno UPLOAD_PATH no está configurada o está vacía.");
        }
    }
}
