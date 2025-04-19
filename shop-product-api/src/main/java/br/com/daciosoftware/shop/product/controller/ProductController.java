package br.com.daciosoftware.shop.product.controller;

import br.com.daciosoftware.shop.models.dto.product.ProductDTO;
import br.com.daciosoftware.shop.models.dto.product.ProductReportRequestDTO;
import br.com.daciosoftware.shop.product.service.ProductService;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> findProductsByCategory(@PathVariable Long categoryId) {
        return productService.findByCategory(categoryId);
    }

    @GetMapping("/{productIdentifier}/identifier")
    public ProductDTO findByIdentifier(@PathVariable String productIdentifier) {
        return productService.findByIdentifier(productIdentifier);
    }

    @GetMapping("/search")
    public List<ProductDTO> findByNone(@RequestParam(name = "nome") String nome) {
        return productService.findByNome(nome);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO save(@RequestBody @Valid ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PatchMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.update(id, productDTO);
    }


    @GetMapping("/pageable")
    public Page<ProductDTO> findAllPageable(Pageable pageable) {
        return productService.findAllPageable(pageable);
    }

    @GetMapping("/all/home")
    public Page<ProductDTO> findAllPageableForHomePage(@RequestParam(required = false, name = "nome") String nome, Pageable pageable) {
        return productService.findAllPageableByName(nome, pageable);
    }

    @PatchMapping("/{id}/upload-photo")
    public ProductDTO uploadPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile file) {
        return productService.uploadPhoto(id, file);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<?> getPhoto(@PathVariable Long id) throws IOException {
        Optional<InputStream> optionalInputStream = productService.getPhoto(id);
        HttpHeaders headers = new HttpHeaders();
        if (optionalInputStream.isPresent()) {
            InputStream photoStream = optionalInputStream.get();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products-photo-" + id);
            return new ResponseEntity<>(IOUtils.toByteArray(photoStream), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(new byte[0], headers, HttpStatus.OK);
    }

    @PostMapping("/report-pdf")
    public ResponseEntity<?> reportPdf(@RequestBody ProductReportRequestDTO productDTO) {
        ByteArrayOutputStream pdfStream = productService.geraReportPdf(productDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "Product service is health !!!";
    }

}
