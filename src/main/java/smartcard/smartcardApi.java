package smartcard;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

/**
 * @author Henri
 */
public class smartcardApi {

    static public List<CardTerminal> getTerminals() throws CardException {
        return TerminalFactory.getDefault().terminals().list();

    }

    static public String toString(byte[] byteTab) {
        String texte = "";
        String hexNombre;
        int i;
        for (i = 0; i < byteTab.length; i++) {
            hexNombre = "";
            hexNombre = Integer.toHexString(byteTab[i]);
            if (hexNombre.length() == 1) {
                texte += " 0" + hexNombre;
            } else {
                texte += " " + hexNombre;
            }
        }
        return texte;
    }

    static public int authCSCDefault(CardChannel channel, int CSCid) throws InvalidSecretCodeException, UnknownModeException, InvalidLcValueException, MaxPresentationExceededException, InvalidP2ParameterException, InvalidInstructionByteException, UnknownException {

        byte[] bytes;
        if (CSCid == 0) {
            CSCid = 0x07;
            bytes = ByteBuffer.allocate(4).putInt(0xAAAAAAAA).array();
        } else if (CSCid == 1) {
            CSCid = 0x39;
            bytes = ByteBuffer.allocate(4).putInt(0x11111111).array();
        } else if (CSCid == 2) {
            CSCid = 0x3B;
            bytes = ByteBuffer.allocate(4).putInt(0x22222222).array();
        } else return -1;


        CommandAPDU command = new CommandAPDU(0x00, 0x20, 0x00, CSCid, bytes);
        ResponseAPDU r;
        try {
            r = channel.transmit(command);
            String text = toString(r.getData());
            System.out.println(text);
            int SW1 = r.getSW1();
            if (SW1 == 144) {
                System.out.println("Auth Success");
                return 0;
            } else if (SW1 == 99) throw new InvalidSecretCodeException("Error : Invalid secret code");
            else if (SW1 == 101) throw new UnknownModeException("Error : Requested mode is unknown");
            else if (SW1 == 103) throw new InvalidLcValueException("Error : Invalid Lc value");
            else if (SW1 == 105)
                throw new MaxPresentationExceededException("Error : Maximum secret code presentation exceeded");
            else if (SW1 == 107) throw new InvalidP2ParameterException("Error : Invalid Lc value");
            else if (SW1 == 109) throw new InvalidInstructionByteException("Error : Invalid Lc value");
            else throw new UnknownException("Error : Encountered an unknown response to authentication attempt");
        } catch (CardException e) {
            e.printStackTrace();
            return -1;
        }
    }

    static public int authCSC(CardChannel channel, int CSCid, int password) throws InvalidSecretCodeException, UnknownModeException, InvalidLcValueException, MaxPresentationExceededException, InvalidP2ParameterException, InvalidInstructionByteException, UnknownException, InvalidNumberOfDigitsException {

        if (CSCid == 0) CSCid = 0x07;
        else if (CSCid == 1) CSCid = 0x39;
        else if (CSCid == 2) CSCid = 0x3B;
        else return -1;

        if (utils.countDigits(password) != 6) {
            throw new InvalidNumberOfDigitsException("Error : wrong digit password format");
        } else {
            byte[] bytes = ByteBuffer.allocate(4).putInt(password).array();
            return verify(channel, CSCid, bytes);
        }
    }

    public static int writeCSC(CardChannel channel, int CSCid, int digitPassword) throws InvalidNumberOfDigitsException, InvalidCSCIdException, InvalidLcValueException, InvalidP2ParameterException, InvalidInstructionByteException, MemoryErrorException, SecurityNotSatisfiedException, UnknownException {

        if (CSCid == 0) CSCid = 0x06;
        else if (CSCid == 1) CSCid = 0x38;
        else if (CSCid == 2) CSCid = 0x3A;
        else throw new InvalidCSCIdException("Error : CSC id must be 0, 1 or 2");

        if (utils.countDigits(digitPassword) != 6) {
            throw new InvalidNumberOfDigitsException("Error : wrong digit password format");
        } else {
            byte[] bytes = ByteBuffer.allocate(4).putInt(digitPassword).array();
            return update(channel, CSCid, 0x04, bytes);
        }
    }

    public static String readUserData(CardChannel channel, int userId) throws InvalidP2ParameterException, InvalidLenghtOfExpectedDataException, UnknownModeException, InvalidInstructionByteException, SecurityNotSatisfiedException, UnknownException {
        int p2 = 0;
        if (userId == 1) p2 = 0x10;
        else if (userId == 2) p2 = 0x28;
        else
            throw new InvalidP2ParameterException("Error : User id must be 1 or 2 in order to match with register adress");

        byte[] readResult = read(channel, p2, 0x40);

        int limit = 0;
        check:
        for (int i = 0; i < readResult.length; i++) {
            if (readResult[i] == 0x00) {
                limit = i;
                break check;
            }
        }

        readResult = Arrays.copyOfRange(readResult, 0, limit);
        // To print the retrieved bytes from card.
        System.out.println(toString(readResult));
        String userData = new String(readResult);
        // To print the text from the retrieved bytes.
        System.out.println(userData);
        userData.replace(" ", "");
        return userData;
    }

    public static int updateUserData(CardChannel channel, int userId, String data) throws InvalidLcValueException, UnknownException, InvalidP2ParameterException, MemoryErrorException, SecurityNotSatisfiedException, InvalidInstructionByteException {
        int p2 = 0;
        if (userId == 1) p2 = 0x10;
        else if (userId == 2) p2 = 0x28;
        else
            throw new InvalidP2ParameterException("Error : User id must be 1 or 2 in order to match with register adress");

        byte[] b = data.getBytes();
        String s = new String(b);
        System.out.println(s);

        int writeResult = update(channel, p2, 0x04, b);
        return writeResult;
    }

    public static int resetUserData(CardChannel channel, int userId) throws InvalidLcValueException, UnknownException, InvalidP2ParameterException, MemoryErrorException, SecurityNotSatisfiedException, InvalidInstructionByteException {
        int p2 = 0;
        if (userId == 1) p2 = 0x10;
        else if (userId == 2) p2 = 0x28;
        else
            throw new InvalidP2ParameterException("Error : User id must be 1 or 2 in order to match with register adress");

        byte[] b = new byte[64];

        int writeResult = update(channel, p2, 0x04, b);
        return writeResult;
    }

    public static int applyUserMode(CardChannel channel) throws InvalidLcValueException, UnknownException, InvalidP2ParameterException, MemoryErrorException, SecurityNotSatisfiedException, InvalidInstructionByteException {
        byte[] issuerToUserCommand = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x80};
        return update(channel, 0x04, 0x04, issuerToUserCommand);
    }

    public static CardChannel resetSmartcardConnexion(CardChannel channel, CardTerminal terminal, Card card) throws CardException {
        System.out.println("ResetSmartCardConnexion :");
        System.out.println("    closing current channel");
        System.out.println("    disconnecting the card with a connection reset");
        card.disconnect(true);
        System.out.println("    connecting with card on T=0");
        card = terminal.connect("T=0");
        System.out.println("    returning card channel");
        return card.getBasicChannel();
    }

    /***
     *
     * Returns -10 if failed to authenticate.
     * Returns -11 if failed to update user info.
     * Returns -12 if provided infos are too long.
     * Retunrs -13 if failed to update user password.
     * Returns -14 if failed to switch to user mode.
     * Returns -15 if failed to reset card.
     *
     * @param channel
     * @param info
     * @return
     */
    public static int createUserCard(CardChannel channel, String info, int userPassword, CardTerminal terminal, Card card) {
        System.out.println("Create user card from blank smartcard");

        System.out.println("      Authenticating as default CSC0 user");
        try {
            authCSCDefault(channel, 0);
            System.out.println("      Authentication success");
        } catch (InvalidSecretCodeException | UnknownModeException | InvalidLcValueException | MaxPresentationExceededException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException e) {
            System.out.println("      Error : Authentication as CSC0 failed");
            if (e.getClass() == InvalidSecretCodeException.class) {
                System.out.println("        Wrong password provided. Current card is not factory");
            }
            e.getMessage();
            return -10;
        }

        System.out.println("      Writing user info in User1");
        if (info.length() < 64) {
            try {
                updateUserData(channel, 1, info);
                System.out.println("      Writing info success");
            } catch (InvalidLcValueException | UnknownException | InvalidP2ParameterException | MemoryErrorException | SecurityNotSatisfiedException | InvalidInstructionByteException e) {
                System.out.println("      Error : failed to write user info");
                e.printStackTrace();
                return -11;
            }
        } else {
            System.out.println("      Error : given info are too long");
            return -12;
        }

        System.out.println("      Updating user password");
        try {
            writeCSC(channel, 1, userPassword);
            System.out.println("      Updating user password success");
        } catch (InvalidNumberOfDigitsException | InvalidCSCIdException | InvalidLcValueException | InvalidP2ParameterException | InvalidInstructionByteException | MemoryErrorException | SecurityNotSatisfiedException | UnknownException e) {
            System.out.println("      Error : failed to update user password");
            e.printStackTrace();
            return -13;
        }

        System.out.println("      Applying user mode");
        try {
            applyUserMode(channel);
            System.out.println("      Applying user mode success");
        } catch (InvalidLcValueException | UnknownException | InvalidP2ParameterException | MemoryErrorException | SecurityNotSatisfiedException | InvalidInstructionByteException e) {
            System.out.println("      Error : failed to apply user mode");
            e.printStackTrace();
            return -14;
        }

        System.out.println("      Reseting card");
        try {
            resetSmartcardConnexion(channel, terminal, card);
            System.out.println("      Reseting card success");
        } catch (CardException e) {
            System.out.println("      Error : failed to reset card");
            e.printStackTrace();
            return -15;
        }
        System.out.println("      Successfully went through user card creation sequence");
        return 0;
    }

    private static byte[] read(CardChannel channel, int p2, int le) throws UnknownModeException, InvalidLenghtOfExpectedDataException, SecurityNotSatisfiedException, InvalidP2ParameterException, InvalidInstructionByteException, UnknownException {
        CommandAPDU command = new CommandAPDU(0x80, 0xBE, 0x00, p2, le);
        ResponseAPDU r;
        try {
            r = channel.transmit(command);
            String text = toString(r.getData());
            System.out.println(text);
            int SW1 = r.getSW1();
            if (SW1 == 144) {
                System.out.println("Read successfully executed");
                return r.getData();
            } else if (SW1 == 101) throw new UnknownModeException("Error : Encoutered a memory error");
            else if (SW1 == 103)
                throw new InvalidLenghtOfExpectedDataException("Error : Invalid lenght of expected data");
            else if (SW1 == 105)
                throw new SecurityNotSatisfiedException("Error : wrong word balance update order, flag update attempt or security issue");
            else if (SW1 == 107) throw new InvalidP2ParameterException("Error : Invalid Lc value");
            else if (SW1 == 109) throw new InvalidInstructionByteException("Error : Invalid Lc value");
            else throw new UnknownException("Error : Encountered an unknown response to update attempt");

        } catch (CardException e) {
            e.printStackTrace();
            throw new UnknownException("Error : Encountered an unknown response to update attempt");
        }
    }

    private static int update(CardChannel channel, int p2, int lc, byte[] data) throws MemoryErrorException, InvalidLcValueException, SecurityNotSatisfiedException, InvalidP2ParameterException, InvalidInstructionByteException, UnknownException {
        CommandAPDU command = new CommandAPDU(0x80, 0xDE, 0x00, p2, data, lc);
        ResponseAPDU r;
        try {
            r = channel.transmit(command);
            String text = toString(r.getData());
            System.out.println(text);
            int SW1 = r.getSW1();
            if (SW1 == 144) {
                System.out.println("Update successfully executed");
                return 0;
            } else if (SW1 == 101) throw new MemoryErrorException("Error : Encoutered a memory error");
            else if (SW1 == 103) throw new InvalidLcValueException("Error : Invalid Lc value");
            else if (SW1 == 105)
                throw new SecurityNotSatisfiedException("Error : wrong word balance update order, flag update attempt or security issue");
            else if (SW1 == 107) throw new InvalidP2ParameterException("Error : Invalid Lc value");
            else if (SW1 == 109) throw new InvalidInstructionByteException("Error : Invalid Lc value");
            else throw new UnknownException("Error : Encountered an unknown response to update attempt");
        } catch (CardException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int verify(CardChannel channel, int p2, byte[] data) throws InvalidSecretCodeException, UnknownModeException, InvalidLcValueException, MaxPresentationExceededException, InvalidP2ParameterException, InvalidInstructionByteException, UnknownException {
        CommandAPDU command = new CommandAPDU(0x00, 0x20, 0x00, p2, data);
        ResponseAPDU r;
        try {
            r = channel.transmit(command);
            String text = toString(r.getData());
            System.out.println(text);
            int SW1 = r.getSW1();
            if (SW1 == 144) {
                System.out.println("Auth Success");
                return 0;
            } else if (SW1 == 99) throw new InvalidSecretCodeException("Error : Invalid secret code");
            else if (SW1 == 101) throw new UnknownModeException("Error : Requested mode is unknown");
            else if (SW1 == 103) throw new InvalidLcValueException("Error : Invalid Lc value");
            else if (SW1 == 105)
                throw new MaxPresentationExceededException("Error : Maximum secret code presentation exceeded");
            else if (SW1 == 107) throw new InvalidP2ParameterException("Error : Invalid Lc value");
            else if (SW1 == 109) throw new InvalidInstructionByteException("Error : Invalid Lc value");
            else throw new UnknownException("Error : Encountered an unknown response to authentication attempt");
        } catch (CardException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) throws CardException {
        List<CardTerminal> terminauxDispos = smartcardApi.getTerminals();
        CardTerminal terminal = terminauxDispos.get(0);
        System.out.println(terminal.toString());
        Card carte = terminal.connect("T=0");
        System.out.println(toString(carte.getATR().getBytes()));
        CardChannel channel = carte.getBasicChannel();

        String infos = "userinfo;on;card";
        int password = 123456;

        //createUserCard(channel, infos, password, terminal, carte );

        try {
            authCSC(channel, 1, password);
            read(channel, 0x04, 0x04);
        } catch (InvalidSecretCodeException | UnknownModeException | InvalidLcValueException | MaxPresentationExceededException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException | InvalidNumberOfDigitsException | InvalidLenghtOfExpectedDataException | SecurityNotSatisfiedException e) {
            e.printStackTrace();
        }


        //runTest3(channel);
        /*
        try {

            byte[] b = read(channel, 0x07, 0x04);
            System.out.println(toString(b));
            String scr = new String(b);
            System.out.println(scr);
        } catch (UnknownModeException | InvalidLenghtOfExpectedDataException | SecurityNotSatisfiedException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException e) {
            e.printStackTrace();
        }*/

        carte.disconnect(false);

    }

    private static void runTest0(CardChannel channel) {
        CommandAPDU commande = new CommandAPDU(0x80, 0xBE, 0x01, 0x00, 0x04);
        ResponseAPDU r = null;
        try {
            r = channel.transmit(commande);
        } catch (CardException e) {
            e.printStackTrace();
        }
        System.out.println("reponse : " + (byte) r.getData()[0]);
        String text = toString(r.getData());
        System.out.println(text);
    }

    private static void runTest1(CardChannel channel) {
        int authResult = -1;
        try {
            authResult = authCSC(channel, 0, 123456);
        } catch (InvalidSecretCodeException | UnknownModeException | InvalidLcValueException | MaxPresentationExceededException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException | InvalidNumberOfDigitsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(authResult);

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        try {
            int writeResult = writeCSC(channel, 0, 234567);
            System.out.println("Write result = " + writeResult);
        } catch (InvalidNumberOfDigitsException | InvalidCSCIdException | InvalidLcValueException | InvalidP2ParameterException | InvalidInstructionByteException | MemoryErrorException | SecurityNotSatisfiedException | UnknownException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void runTest2(CardChannel channel) {

        try {
            byte[] readResult = read(channel, 0x10, 0x04);
            System.out.println(toString(readResult));
        } catch (UnknownModeException | InvalidLenghtOfExpectedDataException | SecurityNotSatisfiedException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");


        try {
            int authResult = authCSC(channel, 0, 234567);
            System.out.println(authResult);
        } catch (InvalidSecretCodeException | UnknownModeException | InvalidLcValueException | MaxPresentationExceededException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException | InvalidNumberOfDigitsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        String original = "drheinrich;passsword";
        byte[] b = original.getBytes();
        System.out.println(toString(original.getBytes()));
        String s = new String(b);
        System.out.println(s);

        try {
            int writeResult = update(channel, 0x10, 0x04, b);
            System.out.println(writeResult);
        } catch (MemoryErrorException | InvalidLcValueException | SecurityNotSatisfiedException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException e) {
            e.printStackTrace();
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");


        try {
            byte[] readResult = read(channel, 0x10, 0x40);
            int limit = 0;
            check:
            for (int i = 0; i < readResult.length; i++) {
                if (readResult[i] == 0x00) {
                    limit = i;
                    break check;
                }
            }

            readResult = Arrays.copyOfRange(readResult, 0, limit);

            System.out.println(toString(readResult));
            String scr = new String(readResult);
            System.out.println(scr);
            scr.replace(" ", "");
            System.out.println(scr.length());
        } catch (UnknownModeException | InvalidLenghtOfExpectedDataException | SecurityNotSatisfiedException | InvalidP2ParameterException | InvalidInstructionByteException | UnknownException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }

    private static void runTest3(CardChannel channel) {
        try {
            System.out.println("Authenticating as User0");
            System.out.println(authCSC(channel, 0, 234567));
            System.out.println("Updating user 1 :");
            updateUserData(channel, 1, "archaon;password");
            System.out.println("Reading user 1 :");
            System.out.println(readUserData(channel, 1));
            System.out.println("Resetting user 1 :");
            resetUserData(channel, 1);
            System.out.println("Reading user 1 :");
            System.out.println(readUserData(channel, 1));

        } catch (InvalidLcValueException | UnknownException | InvalidP2ParameterException | MemoryErrorException | SecurityNotSatisfiedException | InvalidInstructionByteException | InvalidLenghtOfExpectedDataException | UnknownModeException | InvalidSecretCodeException | MaxPresentationExceededException | InvalidNumberOfDigitsException e) {
            e.printStackTrace();
        }
    }

    public static class InvalidNumberOfDigitsException extends Exception {
        public InvalidNumberOfDigitsException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidCSCIdException extends Exception {
        public InvalidCSCIdException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidSecretCodeException extends Exception {
        public InvalidSecretCodeException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class UnknownModeException extends Exception {
        public UnknownModeException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidLcValueException extends Exception {
        public InvalidLcValueException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class MaxPresentationExceededException extends Exception {
        public MaxPresentationExceededException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidP2ParameterException extends Exception {
        public InvalidP2ParameterException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidInstructionByteException extends Exception {
        public InvalidInstructionByteException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class MemoryErrorException extends Exception {
        public MemoryErrorException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class SecurityNotSatisfiedException extends Exception {
        public SecurityNotSatisfiedException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidLenghtOfExpectedDataException extends Exception {
        public InvalidLenghtOfExpectedDataException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class UnknownException extends Exception {
        public UnknownException(String errorMessage) {
            super(errorMessage);
        }
    }
}