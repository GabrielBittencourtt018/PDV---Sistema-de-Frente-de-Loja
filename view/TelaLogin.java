package br.com.logica.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TelaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;



    public TelaLogin() {
        setTitle("Login - Sistema PDV");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel(" Operador (Usuário): "));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel(" Senha: "));
        txtSenha = new JPasswordField();
        add(txtSenha);

        JButton btnEntrar = new JButton("Entrar");
        add(btnEntrar);



        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String senha = new String(txtSenha.getPassword());

                if (usuario.equals("usuarioadmin") && senha.equals("07091822")) {
                    dispose();
                    TelaPDV pdv = new TelaPDV();
                    pdv.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(TelaLogin.this, "Usuário ou Senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}