package br.com.logica.controller;

import br.com.logica.model.ItemVenda;
import br.com.logica.model.Produto;
import br.com.logica.model.Venda;
import br.com.logica.service.Caixa;

import java.util.ArrayList;
import java.util.List;



public class VendaController {
    private List<Produto> estoqueProdutos;
    private Venda vendaAtual;
    private Caixa caixa;


    private List<String> clientesCpf = new ArrayList<>();
    private List<String> clientesNome = new ArrayList<>();
    private List<Double> historicoVendas = new ArrayList<>();


    public VendaController() {
        estoqueProdutos = new ArrayList<>();
        caixa = new Caixa();
        vendaAtual = new Venda();
        inicializarEstoqueExemplo();
    }




    private void inicializarEstoqueExemplo() {
        estoqueProdutos.add(new Produto(1, "Refrigerante", 7.50, 20));
        estoqueProdutos.add(new Produto(2, "Chocolate", 4.00, 35));
        estoqueProdutos.add(new Produto(3, "Água", 2.50, 50));
    }



    public void cadastrarClienteSemClasse(String cpf, String nome) {
        clientesCpf.add(cpf);
        clientesNome.add(nome);
    }



    public List<String> getClientesCpf() { return clientesCpf; }
    public List<String> getClientesNome() { return clientesNome; }
    public List<Double> getHistoricoVendas() { return historicoVendas; }



    public Produto buscarProdutoPorCodigo(int codigo) {
        for (Produto p : estoqueProdutos) {
            if (p.getCodigo() == codigo) return p;
        }
        return null;
    }



    public String adicionarItemAVenda(int codigo, int quantidade) {
        Produto produto = buscarProdutoPorCodigo(codigo);
        if (produto == null) return "Produto não encontrado!";

        ItemVenda itemExistente = null;
        for (ItemVenda iv : vendaAtual.getItens()) {
            if (iv.getProduto().getCodigo() == codigo) {
                itemExistente = iv;
                break;
            }
        }

        int qtdNoCarrinho = (itemExistente != null) ? itemExistente.getQuantidade() : 0;

        if (produto.getEstoque() < (qtdNoCarrinho + quantidade)) {
            return "Estoque insuficiente! Disponível em estoque: " + (produto.getEstoque() - qtdNoCarrinho);
        }

        if (itemExistente != null) {
            itemExistente.adicionarQuantidade(quantidade);
        } else {
            ItemVenda item = new ItemVenda(produto, quantidade);
            vendaAtual.adicionarItem(item);
        }
        return "Sucesso";
    }



    public double obterSubtotalVenda() { return vendaAtual.calcularTotal(); }


    public double calcularTotalComDesconto(double percentualDesconto) {
        double subtotal = obterSubtotalVenda();
        double desconto = subtotal * (percentualDesconto / 100.0);
        return subtotal - desconto;
    }


    public double calcularTroco(double valorPago, double percentualDesconto) {
        double totalComDesconto = calcularTotalComDesconto(percentualDesconto);
        return caixa.calcularTroco(valorPago, totalComDesconto);
    }




    public void finalizarVenda(double totalComDesconto) {
        for (ItemVenda item : vendaAtual.getItens()) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }
        historicoVendas.add(totalComDesconto);
    }



    public void novaVenda() { vendaAtual = new Venda(); }
    public Venda getVendaAtual() { return vendaAtual; }
    public List<Produto> getEstoqueProdutos() { return estoqueProdutos; }
}