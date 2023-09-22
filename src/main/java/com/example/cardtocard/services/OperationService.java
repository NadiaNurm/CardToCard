package com.example.cardtocard.services;

import com.example.cardtocard.Util.OperationUtil;
import com.example.cardtocard.dto.OperationDTO;
import com.example.cardtocard.models.Operation;
import com.example.cardtocard.repo.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OperationService {
    @Autowired
    OperationRepository operationRepository;

    public void saveOperation(Operation operation){
        operationRepository.save(operation);
    }
    public List<Operation> allOperations(){
        return operationRepository.findAll();
    }
    public List<Operation> allCardOperations(Long base_id,Long target_id){
        return operationRepository.findOperationsNative(base_id,target_id);
    }

    public List<OperationDTO> targetCards ( String targetCardNumber){
        List<Operation> operations = operationRepository.findAll();
        List<OperationDTO> operationDTOList = OperationUtil.crteateOperationDTO(operations);
        List<OperationDTO> result = new ArrayList<>();
        for (OperationDTO o : operationDTOList ) {
            if(o.getTargetCardNumber().equals(targetCardNumber)){
                result.add(o);
            }
        }
        return result;
    }

    public Set<String> allTargetCards(){
        List<Operation> operations = operationRepository.findAll();
        List<OperationDTO> operationDTOList = OperationUtil.crteateOperationDTO(operations);
        return operationDTOList.stream().map(OperationDTO::getTargetCardNumber).collect(Collectors.toSet());
    }

    public List<OperationDTO> filterOperations(int min, int max) {
        List<Operation> operations = operationRepository.findAll();
        List<OperationDTO> operationDTOList = OperationUtil.crteateOperationDTO(operations);
        List<OperationDTO> filtered = new ArrayList<>();
        for (OperationDTO op:operationDTOList) {
            if (op.getAmount() >= min & op.getAmount() <= max) {
                filtered.add(op);
            }

        }
        return filtered;
        //return operationDTOList.stream()
        //        .filter(op -> op.getAmount() >= min && op.getAmount() <= max)
        //        .collect(Collectors.toList());
    }
}
