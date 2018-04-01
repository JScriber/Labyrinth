package fr.imie.labyrinth.symbol;

public enum Symbol {

    WALL ("X"),
    LANE ("_"),
    START ("S"),
    END ("G");

    private String name;


    Symbol(String name){
        this.name = name;
    }

    public String toString(){
        return this.name;
    }
}
