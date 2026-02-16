package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    /**
     * Guarda el archivo en el sistema de ficheros.
     * @param file Archivo recibido del formulario.
     * @return El nombre del archivo generado (UUID + extensión) o null si falla.
     */
    public String saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("No se puede guardar un archivo vacío.");
            }

            // Normalizar el nombre del archivo
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

            // Generar un nombre único para evitar colisiones
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

            // Resolver la ruta completa donde se guardará
            Path path = Paths.get(uploadPath).resolve(fileName);

            // Crear el directorio si no existe
            Files.createDirectories(path.getParent());

            // Copiar el archivo (si existe uno con el mismo nombre, lo reemplaza)
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Archivo guardado correctamente: {}", fileName);
            return fileName;

        } catch (IOException e) {
            logger.error("Error al guardar el archivo: {}", e.getMessage());
            return null; // O lanzar una excepción personalizada
        }
    }

    /**
     * Elimina un archivo del sistema de ficheros.
     * @param fileName Nombre del archivo a eliminar.
     */
    public void deleteFile(String fileName) {
        try {
            Path path = Paths.get(uploadPath).resolve(fileName);
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                logger.info("Archivo eliminado: {}", fileName);
            } else {
                logger.warn("El archivo no existía o no se pudo eliminar: {}", fileName);
            }
        } catch (IOException e) {
            logger.error("Error al eliminar el archivo {}: {}", fileName, e.getMessage());
        }
    }
}
