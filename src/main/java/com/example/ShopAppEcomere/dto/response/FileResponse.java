package com.example.ShopAppEcomere.dto.response;


import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private String fileName;
    private Instant uploadedAt;
}
