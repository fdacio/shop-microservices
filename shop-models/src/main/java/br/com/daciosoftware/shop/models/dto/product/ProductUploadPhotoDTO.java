package br.com.daciosoftware.shop.models.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUploadPhotoDTO {

	public ProductDTO productDTO;
	public String fileName;
	public String fileDownload;
	public Integer fileSize;
}
