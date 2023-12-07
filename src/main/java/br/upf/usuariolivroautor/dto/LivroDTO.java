package br.upf.usuariolivroautor.dto;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//notacoes referentes ao lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "tb_livro")
public class LivroDTO {

		@Id		
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Column(name = "livro_nome", nullable = false)
		private String nome;
		
		@Column(name = "livro_lancamento")
		private Date dataLancamento;
		
		@ManyToOne // definindo a relação muitos pra um
		@JoinColumn(name = "autor_id")
		private AutorDTO autor;
}
