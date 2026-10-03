package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.BedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BedResponse {
    private Long id;
    private Long wardId;
    private String wardName;
    private String bedNumber;
    private BedStatus status;
}
