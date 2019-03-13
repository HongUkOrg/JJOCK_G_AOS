package com.david.user.sealseeksee.LetterAdapter;

public class LetterOption {

    public final String description;
    public final String amount;

    public LetterOption(String amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return "DonationOption{" +
                "description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}