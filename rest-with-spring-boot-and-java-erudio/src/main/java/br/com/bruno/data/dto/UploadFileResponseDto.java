package br.com.bruno.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponseDto {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;
}
