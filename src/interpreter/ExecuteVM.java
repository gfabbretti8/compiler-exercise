package interpreter;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class ExecuteVM {
    public static final int CODESIZE = 10000;
    public static final int MEMSIZE = 10000;
    private Node[] code;
    private int[] memory = new int[MEMSIZE];
    private int ip = 0;         // ip = instruction pointer
    private int sp = MEMSIZE-1;   // sp = stack pointer
    private int fp = MEMSIZE-1;   // fp = frame pointer
    private int ra;             // ra = return address
    private int al;             // al = access link
    private int a;              // a = stores the value of an exp
    private int t;              // t = temporary register

    public ExecuteVM(Node[] code) {
        this.code = code;
    }

    public void run() {
        while (true) {
            Node bytecode = code[ip++]; // fetch
            int r1, r2;
            boolean b1, b2;
            int address;
            int offset;
            int i;

            switch (bytecode.getInstr()) {
                case ("print"):
                    System.out.println("\n OUTPUT: " + getRegister(bytecode.getArg1()));
                    break;
                case ("li"):
                    i = getIntFromString(bytecode.getArg2());
                    setRegister(bytecode.getArg1(), i);
                    break;
                case ("lw"):
                    r2 = getRegister(bytecode.getArg2());
                    offset = bytecode.getOffset();
                    r1 = memory[r2 + offset];
                    setRegister(bytecode.getArg1(), r1);
                    break;
                case ("sw"):
                    r1 = getRegister(bytecode.getArg1());
                    r2 = getRegister(bytecode.getArg2());
                    offset = bytecode.getOffset();
                    memory[r2 + offset] = r1;
                    break;
                case ("move"):
                    r2 = getRegister(bytecode.getArg2());
                    setRegister(bytecode.getArg1(), r2);
                    break;
                case ("addi"):
                    r2 = getRegister(bytecode.getArg2());
                    i = getIntFromString(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), r2+i);
                    break;
                case ("add"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), r1+r2);
                    break;
                case ("sub"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), r1-r2);
                    break;
                case ("time"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), r1*r2);
                    break;
                case ("divide"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), r2/r1);
                    break;
                case ("and"):
                    b1 = getBoolFromString(getRegister(bytecode.getArg2()));
                    b2 = getBoolFromString(getRegister(bytecode.getArg3()));
                    setRegister(bytecode.getArg1(), getIntFromBool(b1&&b2));
                    break;
                case ("or"):
                    b1 = getBoolFromString(getRegister(bytecode.getArg2()));
                    b2 = getBoolFromString(getRegister(bytecode.getArg3()));
                    setRegister(bytecode.getArg1(), getIntFromBool(b1||b2));
                    break;
                case ("eq"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), getIntFromBool(r1==r2));
                    break;
                case ("noteq"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), getIntFromBool(r1!=r2));
                    break;
                case ("smaller"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), getIntFromBool(r1<r2));
                    break;
                case ("greater"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), getIntFromBool(r1>r2));
                    break;
                case ("smalleq"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), getIntFromBool(r1<=r2));
                    break;
                case ("greateq"):
                    r1 = getRegister(bytecode.getArg2());
                    r2 = getRegister(bytecode.getArg3());
                    setRegister(bytecode.getArg1(), getIntFromBool(r1>=r2));
                    break;
                case ("beq"):
                    r1 = getRegister(bytecode.getArg1());
                    r2 = getRegister(bytecode.getArg2());
                    if (r1 == r2){
                        setRegister("ip", bytecode.getOffset());
                    }
                    break;
                case ("b"):
                    setRegister("ip", bytecode.getOffset());
                    break;
                case ("halt"):
                    return;
                default:
                    break;
            }
        }
    }

    private int getRegister(String register){
        switch (register){
            case("sp"):
                return sp;
            case("fp"):
                return fp;
            case("ra"):
                return ra;
            case("al"):
                return al;
            case("a"):
                return a;
            case("t"):
                return t;
            default:
                System.out.println("Registro non trovato!");
                return 0;
        }
    }

    private void setRegister(String register, Integer value){
        switch (register){
            case("sp"):
                sp = value;
                break;
            case("fp"):
                fp = value;
                break;
            case("ip"):
                ip = value;
                break;
            case("ra"):
                ra = value;
                break;
            case("al"):
                al = value;
                break;
            case("a"):
                a = value;
                break;
            case("t"):
                t = value;
                break;
            default:
                System.out.println("Errore setRegister()");
        }
    }

    private int getIntFromString(String arg){
        return Integer.parseInt(arg);
    }

    private boolean getBoolFromString(Integer value){
        switch (value){
            case 1:
                return true;
            case 0:
                return false;
            default:
                System.out.println("Errore getBoolFromString()");
                return false;
        }
    }

    private int getIntFromBool(Boolean value) {
        if (value) {
            return 1;
        } else {
            return 0;
        }
    }
}