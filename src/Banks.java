import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

class Account implements Serializable {
    private double balance;
    private int accNumber;

    public Account(int a) {
        balance = 0.0;
        accNumber = a;
    }

    public void deposit(double sum) {
        balance += sum;
    }

    public void withdraw(double sum) {

        if (sum > balance) {
            System.out.println("ERROR");
        } else {
            balance -= sum;
        }

    }

    public double getBalance() {
        return balance;
    }

    public int getAccountNumber() {
        return accNumber;
    }

    public void transfer(double amount, Account other) {
        withdraw(amount);
        other.deposit(amount);
    }

    public String toString() {
        return "Account Number: " + accNumber + ", Balance: $" + balance;
    }

    public final void print() {
        System.out.println(toString());
    }
}

class SavingsAccount extends Account implements Serializable {
    private double interestRate;

    public SavingsAccount(int a, double rate) {
        super(a);
        interestRate = rate;
    }

    public void addInterest() {
        deposit(getBalance() * interestRate);
    }

    @Override
    public String toString() {
        return super.toString() + ", Interest Rate: " + interestRate * 100 + "%";
    }
}

class Currency implements Serializable {
    private double value;
    private int multiplier;
    private String code;

    public Currency(double value, int multiplier, String code) {
        this.value = value;
        this.multiplier = multiplier;
        this.code = code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "value=" + value +
                ", multiplier=" + multiplier +
                ", code='" + code + '\'' +
                '}';
    }
}

class CurrencyAccount extends Account implements Serializable {
    private Map<String, Double> balanceInCurrency;

    public CurrencyAccount(int a) {
        super(a);
        balanceInCurrency = new HashMap<>();
    }

    public void convertSumToCurrency(double sum, Currency currency) {
        double convertedSum = sum * currency.getValue() * currency.getMultiplier();
        deposit(convertedSum);
        balanceInCurrency.put(currency.getCode(), getBalanceInCurrency(currency.getCode()) + sum);
    }

    public void convertSumFromCurrency(double sum, Currency currency) {
        double convertedSum = sum / (currency.getValue() * currency.getMultiplier());
        withdraw(convertedSum);
        balanceInCurrency.put(currency.getCode(), getBalanceInCurrency(currency.getCode()) - convertedSum);
    }

    public double getBalanceInCurrency(String currencyCode) {
        return balanceInCurrency.getOrDefault(currencyCode, 0.0);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString() + ", Balance in Currency: ");
        for (Map.Entry<String, Double> entry : balanceInCurrency.entrySet()) {
            result.append(entry.getKey()).append(": $").append(entry.getValue()).append(", ");
        }
        return result.toString();
    }
}

class Bank implements Serializable {
    private Vector<Account> accounts;

    public Bank() {
        accounts = new Vector<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void update() {
        for (Account account : accounts) {
            // Add/withdraw money
            account.deposit(100);
            account.withdraw(50);

            // Add interest to Savings accounts
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).addInterest();
            }

            // Convert balance in CurrencyAccounts
            if (account instanceof CurrencyAccount) {
                CurrencyAccount currencyAccount = (CurrencyAccount) account;
                System.out.println(currencyAccount.toString());
                System.out.println("Balance in USD: $" + currencyAccount.getBalanceInCurrency("USD"));
                currencyAccount.convertSumToCurrency(100, new Currency(1.0, 1, "USD"));
                System.out.println("Balance after conversion: " + currencyAccount.toString());
            }
        }
    }

    public void openAccount(Account account) {
        accounts.add(account);
    }

    public void closeAccount(Account account) {
        accounts.remove(account);
    }

    public void displayAccounts() {
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }

    public void serialize(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(this);
            System.out.println("Serialization successful.");
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
        }
    }

    public static Bank deserialize(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (Bank) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Bank bank = new Bank();

        // Create and add some test accounts
        SavingsAccount savingsAccount = new SavingsAccount(1, 0.05);
        CurrencyAccount currencyAccount = new CurrencyAccount(2);

        bank.openAccount(savingsAccount);
        bank.openAccount(currencyAccount);

        // Update and display accounts
        bank.update();
        bank.displayAccounts();

        // Serialize and deserialize
        bank.serialize("bank.ser");
        Bank deserializedBank = deserialize("bank.ser");
        if (deserializedBank != null) {
            System.out.println("\nDeserialized Bank:");
            deserializedBank.displayAccounts();
        }
    }
}

