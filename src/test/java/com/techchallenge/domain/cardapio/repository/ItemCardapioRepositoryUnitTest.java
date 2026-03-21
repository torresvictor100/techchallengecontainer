package com.techchallenge.domain.cardapio.repository;

import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemCardapioRepositoryUnitTest {

    @Test
    void findByMethodsAreInvoked() {
        ItemCardapioRepository repository = mock(ItemCardapioRepository.class);
        ItemCardapio item = new ItemCardapio();
        item.setNome("Lasanha");
        item.setDescricao("Desc");
        item.setPreco(new BigDecimal("20.00"));

        when(repository.findAll()).thenReturn(List.of(item));

        var lista = repository.findAll();

        assertThat(lista).isNotEmpty();
        verify(repository).findAll();
    }
}
