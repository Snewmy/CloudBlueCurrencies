package me.sam;

public class BankAccount {

    int coins;

    public BankAccount() {
        this.coins = 0;
    }

    public BankAccount(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coinsToAdd) {
        this.coins += coinsToAdd;
    }

    public void takeCoins(int coinsToTake) {
        this.coins -= coinsToTake;
    }
}
