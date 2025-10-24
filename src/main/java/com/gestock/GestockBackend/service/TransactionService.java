package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.mappers.TransactionMapper;
import com.gestock.GestockBackend.model.entity.Transaction;
import com.gestock.GestockBackend.model.dto.TransactionRequestDto;
import com.gestock.GestockBackend.model.dto.TransactionResponseDto;
import com.gestock.GestockBackend.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
        Transaction entity = transactionMapper.toEntity(requestDto);
        Transaction saved = transactionRepository.save(entity);
        return transactionMapper.toResponseDto(saved);
    }

    public TransactionResponseDto getTransactionById(Long id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction no encontrada con ID: " + id));
        return transactionMapper.toResponseDto(tx);
    }

    public List<TransactionResponseDto> getAllTransactions() {
        return transactionMapper.toResponseDtoList(transactionRepository.findAll());
    }

    @Transactional //Actualizar datos en la bd
    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto requestDto) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction no encontrada con ID: " + id));
        transactionMapper.updateEntityFromDto(requestDto, existing);
        Transaction saved = transactionRepository.save(existing);
        return transactionMapper.toResponseDto(saved);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
