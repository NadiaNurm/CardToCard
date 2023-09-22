package com.example.cardtocard.Util;

import com.example.cardtocard.dto.OperationDTO;
import com.example.cardtocard.models.Operation;

import java.util.ArrayList;
import java.util.List;

public class OperationUtil {

    public static List<OperationDTO> crteateOperationDTO(List<Operation> operations){
        List<OperationDTO> operationDTOList = new ArrayList<>();
        for (Operation o : operations) {
            operationDTOList.add(new OperationDTO(o.getDate(),o.getCode(),o.getCurrency(),Integer.parseInt(o.getAmount()),o.getBaseCard().getShowNumber(),o.getTargetCard().getShowNumber()));
        }
        return operationDTOList;
    }


}
