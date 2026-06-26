package br.com.logica.view;

import br.com.logica.controller.VendaController;
import br.com.logica.model.ItemVenda;
import br.com.logica.model.Produto;

import javax.swing.*;
import java.awt.*;




public class TelaPDV extends JFrame {
    private VendaController controller;

    private JTextField txtCodigo, txtQuantidade, txtValorPago, txtDesconto, txtPesquisa;
    private JLabel lblSubtotal, lblTotal, lblTroco;
    private JTextArea txtCarrinho;
    private JTextArea txtEstoqueConsulta;
    private JButton btnAdicionar, btnFinalizar;

    public TelaPDV() {
        controller = new VendaController();
        configurarJanela();
        inicializarComponentes();
        atualizarPainelEstoque("");
    }



    private void configurarJanela() {
        setTitle("Sistema - Frente de Loja (PDV)");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }



    private void inicializarComponentes() {
        JPanel panelEstoque = new JPanel(new BorderLayout());
        panelEstoque.setBorder(BorderFactory.createTitledBorder("Produtos Atualmente em Estoque"));

        JPanel panelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPesquisa.add(new JLabel("Pesquisar (Nome ou Código): "));
        txtPesquisa = new JTextField(20);
        JButton btnPesquisar = new JButton("Buscar");
        panelPesquisa.add(txtPesquisa);
        panelPesquisa.add(btnPesquisar);
        panelEstoque.add(panelPesquisa, BorderLayout.NORTH);

        txtEstoqueConsulta = new JTextArea(5, 50);
        txtEstoqueConsulta.setEditable(false);
        txtEstoqueConsulta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelEstoque.add(new JScrollPane(txtEstoqueConsulta), BorderLayout.CENTER);
        add(panelEstoque, BorderLayout.NORTH);


        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Adicionar Produto"));
        panelInput.add(new JLabel("Código do Produto: "));
        txtCodigo = new JTextField();
        panelInput.add(txtCodigo);
        panelInput.add(new JLabel("Quantidade: "));
        txtQuantidade = new JTextField("1");
        panelInput.add(txtQuantidade);

        btnAdicionar = new JButton("Adicionar ao Carrinho");
        panelInput.add(btnAdicionar);
        panelCentro.add(panelInput);

        JPanel panelCarrinho = new JPanel(new BorderLayout());
        panelCarrinho.setBorder(BorderFactory.createTitledBorder("Carrinho de Compras"));
        txtCarrinho = new JTextArea();
        txtCarrinho.setEditable(false);
        txtCarrinho.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelCarrinho.add(new JScrollPane(txtCarrinho), BorderLayout.CENTER);
        panelCentro.add(panelCarrinho);

        add(panelCentro, BorderLayout.CENTER);



        JPanel panelInferior = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel panelFinanceiro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        panelFinanceiro.setBorder(BorderFactory.createTitledBorder("Painel Financeiro"));

        lblSubtotal = new JLabel("Subtotal: R$ 0,00");
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 13));
        panelFinanceiro.add(lblSubtotal);

        panelFinanceiro.add(new JLabel("Desconto (%):"));
        txtDesconto = new JTextField("0", 3);
        panelFinanceiro.add(txtDesconto);

        lblTotal = new JLabel("TOTAL: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelFinanceiro.add(lblTotal);

        panelFinanceiro.add(new JLabel("Valor Pago: R$"));
        txtValorPago = new JTextField(6);
        panelFinanceiro.add(txtValorPago);

        lblTroco = new JLabel("Troco: R$ 0,00");
        lblTroco.setFont(new Font("Arial", Font.BOLD, 16));
        panelFinanceiro.add(lblTroco);

        panelInferior.add(panelFinanceiro);


        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JButton btnNovaVenda = new JButton("Nova Venda");
        JButton btnFinalizar = new JButton("Finalizar Venda");
        JButton btnCancelar = new JButton("Cancelar Venda");
        JButton btnCadastrarCliente = new JButton("Cadastrar Cliente");
        JButton btnVerHistorico = new JButton("Histórico");

        panelBotoes.add(btnNovaVenda);
        panelBotoes.add(btnFinalizar);
        panelBotoes.add(btnCancelar);


        btnCadastrarCliente.addActionListener(e -> {
            String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do Cliente:");
            if (cpf == null || cpf.trim().isEmpty()) return; // Cancelou ou deixou em branco

            String nome = JOptionPane.showInputDialog(this, "Digite o Nome do Cliente:");
            if (nome == null || nome.trim().isEmpty()) return;

            controller.cadastrarClienteSemClasse(cpf.trim(), nome.trim());
            JOptionPane.showMessageDialog(this, "Cliente " + nome + " cadastrado com sucesso!");
        });


        btnVerHistorico.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();


            sb.append("________ CLIENTES CADASTRADOS ________\n");
            if (controller.getClientesNome().isEmpty()) {
                sb.append("Nenhum cliente cadastrado.\n");
            }
            else {
                for (int i = 0; i < controller.getClientesNome().size(); i++) {
                    sb.append(String.format("Nome: %s | CPF: %s\n",
                            controller.getClientesNome().get(i),
                            controller.getClientesCpf().get(i)));
                }
            }

            sb.append("\n________ HISTÓRICO DE VENDAS __________\n");
            if (controller.getHistoricoVendas().isEmpty()) {
                sb.append("Nenhuma venda finalizada ainda.\n");
            }
            else {
                int cont = 1;

                for (Double valor : controller.getHistoricoVendas()) {
                    sb.append(String.format("Venda nº %d - Total Pago: R$ %.2f\n", cont++, valor));
                }
            }

            JTextArea txtArea = new JTextArea(15, 35);
            txtArea.setText(sb.toString());
            txtArea.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(txtArea), "Relatório do Sistema", JOptionPane.INFORMATION_MESSAGE);
        });
        panelBotoes.add(btnCadastrarCliente);
        panelBotoes.add(btnVerHistorico);
        panelInferior.add(panelBotoes);

        add(panelInferior, BorderLayout.SOUTH);


        btnPesquisar.addActionListener(e -> atualizarPainelEstoque(txtPesquisa.getText().trim()));



        btnAdicionar.addActionListener(e -> {

            try {
                int codigo = Integer.parseInt(txtCodigo.getText().trim());
                int qtd = Integer.parseInt(txtQuantidade.getText().trim());
                double descPercent = Double.parseDouble(txtDesconto.getText().trim().replace(",", "."));

                if (qtd <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String resultado = controller.adicionarItemAVenda(codigo, qtd);
                if (resultado.equals("Sucesso")) {
                    atualizarCarrinho();
                    lblSubtotal.setText(String.format("Subtotal: R$ %.2f", controller.obterSubtotalVenda()));
                    lblTotal.setText(String.format("TOTAL: R$ %.2f", controller.calcularTotalComDesconto(descPercent)));
                    txtCodigo.setText("");
                    txtQuantidade.setText("1");
                }
                else {
                    JOptionPane.showMessageDialog(this, resultado, "Aviso!", JOptionPane.WARNING_MESSAGE);
                }
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preencha os campos com valores válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });



        btnFinalizar.addActionListener(e -> {
            try {
                if (controller.getVendaAtual().getItens().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Carrinho vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                double descPercent = Double.parseDouble(txtDesconto.getText().trim().replace(",", "."));
                double valorPago = Double.parseDouble(txtValorPago.getText().trim().replace(",", "."));


                double totalComDesconto = controller.calcularTotalComDesconto(descPercent);
                lblTotal.setText(String.format("TOTAL: R$ %.2f", totalComDesconto));

                if (valorPago < totalComDesconto) {
                    JOptionPane.showMessageDialog(this, "Valor pago insuficiente!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double troco = controller.calcularTroco(valorPago, descPercent);
                lblTroco.setText(String.format("Troco: R$ %.2f", troco));

                controller.finalizarVenda(totalComDesconto);
                JOptionPane.showMessageDialog(this, "Venda finalizada e estoque baixado com sucesso!");

                atualizarPainelEstoque("");
                btnFinalizar.setEnabled(false);
                btnAdicionar.setEnabled(false);
                txtDesconto.setEditable(false);
                txtValorPago.setEditable(false);
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Insira valores financeiros válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });



        btnNovaVenda.addActionListener(e -> {
            controller.novaVenda();
            txtCarrinho.setText("");
            txtValorPago.setText("");
            txtDesconto.setText("0");
            lblSubtotal.setText("Subtotal: R$ 0,00");
            lblTotal.setText("TOTAL: R$ 0,00");
            lblTroco.setText("Troco: R$ 0,00");
            btnFinalizar.setEnabled(true);
            btnAdicionar.setEnabled(true);
            txtDesconto.setEditable(true);
            txtValorPago.setEditable(true);
        });



        btnCancelar.addActionListener(e -> {
            controller.novaVenda();
            txtCarrinho.setText("Venda Cancelada.");
            lblSubtotal.setText("Subtotal: R$ 0,00");
            lblTotal.setText("TOTAL: R$ 0,00");
            lblTroco.setText("Troco: R$ 0,00");
            txtValorPago.setText("");
            txtDesconto.setText("0");
            btnFinalizar.setEnabled(true);
            btnAdicionar.setEnabled(true);
            txtDesconto.setEditable(true);
            txtValorPago.setEditable(true);
        });
    }




    private void atualizarCarrinho() {
        StringBuilder sb = new StringBuilder();
        sb.append("___________ CARRINHO de PRODUTOS ___________\n");
        for (ItemVenda item : controller.getVendaAtual().getItens()) {
            sb.append(String.format("%d x %s (R$ %.2f) = R$ %.2f\n",
                    item.getQuantidade(),
                    item.getProduto().getNome(),
                    item.getProduto().getPreco(),
                    item.getSubtotal()));
        }
        txtCarrinho.setText(sb.toString());
    }




    private void atualizarPainelEstoque(String filtro) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s %-20s %-12s %-10s\n", "Código", "Produto", "Preço", "Estoque"));
        sb.append("------------------------------------------------------------\n");

        for (Produto p : controller.getEstoqueProdutos()) {
            if (!filtro.isEmpty()) {
                String codStr = String.valueOf(p.getCodigo());
                if (!codStr.contains(filtro) && !p.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    continue;
                }
            }
            sb.append(String.format("%-12d %-20s R$ %-9.2f %-10d\n",
                    p.getCodigo(), p.getNome(), p.getPreco(), p.getEstoque()));
        }
        txtEstoqueConsulta.setText(sb.toString());
    }
}