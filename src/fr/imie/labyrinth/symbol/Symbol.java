package fr.imie.labyrinth.symbol;

public enum Symbol {

    // Warning: Changing the characters will prevent old mazes from being solved.
    WALL ("X"),
    LANE ("_"),
    START ("S"),
    END ("G"),
    QUICK ("+");

    private String name;


    Symbol(String name){
        this.name = name;
    }

    public String toString(){
        return this.name;
    }
}
