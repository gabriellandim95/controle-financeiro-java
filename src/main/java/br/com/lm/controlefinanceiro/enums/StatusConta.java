package br.com.lm.controlefinanceiro.enums;

public enum StatusConta {
    EM_ABERTO,
    CANCELADA,
    VENCIDA,
    PAGO,
    PERTO_DE_VENCER;

    public static StatusConta[] getPagoVencidoCancelado(){
        return new StatusConta[]{
                PAGO,
                VENCIDA,
                CANCELADA
        };
    }

}
