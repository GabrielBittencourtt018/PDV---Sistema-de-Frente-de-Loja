package br.com.logica.service;


public class Caixa {
    public double calcularTroco(double valorPago, double totalComDesconto) {
        return valorPago - totalComDesconto;
    }
}