package utils;

import answers.Request;
import data.*;
import exceptions.IncorrectCommandException;
import exceptions.IncorrectScriptInputException;
import exceptions.RecursionScriptException;
import exceptions.WrongAmountOfElements;

import java.time.LocalDateTime;
import java.util.Stack;

/**
 * Validation + making a request class
 * @author NastyaBordun
 * @version 1.1
 */

public class Business {

    /**
     * Name of scripts which are executing now
     */
    private final Stack<String> scriptNames;

    /**
     * Class providing getting correct information from the user {@link AskManager}
     */
    private AskManager askManager;

    /**
     * For special commands
     */
    private Console console = null;


    /**
     * Constructor for class
     * @param askManager Class providing getting correct information from the user {@link AskManager}
     */
    public Business(AskManager askManager){
        this.askManager = askManager;
        scriptNames = new Stack<>();
    }

    /**
     * Adding {@link Console} object to class
     * @param console For special commands
     */
    public void addConsole(Console console){
        this.console = console;
    }


    /**
     * Refactoring string before the execution
     * @param commandArr command line
     * @return refactored String
     * @throws IncorrectCommandException undefined command
     */
    private String refactorString(String[] commandArr) throws IncorrectCommandException {
        if(commandArr.length == 0) throw new IncorrectCommandException("Введена некорректная команда");
        String line;
        if(commandArr.length == 1){
            line = "";
        }
        else{
            line = commandArr[1];
        }
        return line;
    }

    /**
     * Making request to Server
     * @param command user command
     * @return {@link Request} request to Server
     * @throws IncorrectCommandException Incorrect command
     */
    public Request makeRequest(String command, User user) throws IncorrectCommandException {
        String[] commandArr = command.trim().split(" ", 2);
        String line = refactorString(commandArr);

        ArgumentState argumentState = analyzeCommand(commandArr[0], line);
        if(argumentState == ArgumentState.ERROR) return null;

        try {
            SpaceMarineRaw spaceMarineRaw = null;
            switch (argumentState) {
                case ADD_OBJECT: {
                    spaceMarineRaw = addSpaceMarine();
                    return new Request(commandArr[0], line, spaceMarineRaw, user);
                }
                case UPDATE_OBJECT: {
                    spaceMarineRaw = updateSpaceMarine();
                    return new Request(commandArr[0], line, spaceMarineRaw, user);
                }
                case SCRIPT_MODE: {
                    for(String name : scriptNames){
                        if(name.equals(line)){
                            throw new RecursionScriptException("Ошибка: рекурсивный вызов файла-скрипта");
                        }
                    }
                    scriptNames.push(line);
                    Printer.println("---Начато выполнение скрипта---");
                    console.scriptMode(line);
                    scriptNames.pop();
                    break;
                }
                case EXIT:
                    console.setWork(false);
            }
        }
        catch (IncorrectScriptInputException | RecursionScriptException e){
            Printer.printError(e.getMessage());
            return null;
        }
        return new Request(commandArr[0], line, user);
    }

    /**
     * Class for analyzing commands (what we need to add to request or number of arguments)
     * @param command command name
     * @param arg command argument
     * @return {@link ArgumentState}
     */
    public ArgumentState analyzeCommand(String command, String arg){
        try {
            switch (command) {
                case "help":
                case "info":
                case "clear":
                case "reorder":
                case "print_field_ascending_weapon_type":
                case "print_field_descending_achievements":
                case "show":
                    if (arg.length() != 0) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.OK;
                case "remove_greater":
                case "remove_lower":
                case "add":
                    if (arg.length() != 0) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.ADD_OBJECT;
                case "update":
                    if (arg.length() == 0) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.UPDATE_OBJECT;
                case "filter_starts_with_achievements":
                case "remove_by_id":
                    String [] commandArr1 = arg.trim().split(" ");
                    if (arg.length() == 0 || commandArr1.length != 1) {
                        throw new WrongAmountOfElements("Неправильное количество аргументов в команде");
                    }
                    else return ArgumentState.OK;
                case "execute_script":
                    String [] commandArr2 = arg.trim().split(" ");
                    if(arg.length() == 0 || commandArr2.length != 1){
                        throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
                    }
                    else return ArgumentState.SCRIPT_MODE;
                case "exit":
                    if(arg.length() != 0){
                        throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
                    }
                    else return ArgumentState.EXIT;
                default:
                    throw new IncorrectCommandException("Введена некорректная команда");
            }
        }
        catch (WrongAmountOfElements | IncorrectCommandException e){
            Printer.printError(e.getMessage());
            return ArgumentState.ERROR;
        }
    }

    /**
     * Class for creating {@link SpaceMarine} for request (add)
     * @return adding {@link SpaceMarine}
     * @throws IncorrectScriptInputException Something wrong with script
     */
    private SpaceMarineRaw addSpaceMarine() throws IncorrectScriptInputException {
        if(!scriptNames.empty()){
            askManager.setInteractiveMode(false);
        }
        SpaceMarineRaw spaceMarineRaw = new SpaceMarineRaw(
                askManager.askName(),
                askManager.askCoordinates(),
                LocalDateTime.now(),
                askManager.askHealth(),
                askManager.askAchievements(),
                askManager.askWeaponType(),
                askManager.askMeleeWeapon(),
                askManager.askChapter()
                );
        askManager.setInteractiveMode(true);
        return spaceMarineRaw;
    }

    /**
     * Creating {@link SpaceMarine} for request (update)
     * @return updating {@link SpaceMarine}
     * @throws IncorrectScriptInputException Something wrong with script
     */
    private SpaceMarineRaw updateSpaceMarine() throws IncorrectScriptInputException {
        if(!scriptNames.empty()){
            askManager.setInteractiveMode(false);
        }
        String name = null;
        if(askManager.questionCheck("name")) name = askManager.askName();
        long x = 992;
        if(askManager.questionCheck("координата x")) x = askManager.askCoordinateX();
        Double y = null;
        if(askManager.questionCheck("координата y")) y = askManager.askCoordinateY();
        Float health = null;
        if(askManager.questionCheck("health")) health = askManager.askHealth();
        String achievements = null;
        if(askManager.questionCheck("achievements")) achievements = askManager.askAchievements();
        Weapon weaponType = null;
        if(askManager.questionCheck("weaponType")) weaponType = askManager.askWeaponType();
        MeleeWeapon meleeWeapon = null;
        if(askManager.questionCheck("meleeWeapon")) meleeWeapon = askManager.askMeleeWeapon();
        String chapterName = null;
        if(askManager.questionCheck("chapterName")) chapterName = askManager.askChapterName();
        String chapterWorld = null;
        if(askManager.questionCheck("chapterWorld")) chapterWorld = askManager.askChapterWorld();
        Coordinates coordinates = new Coordinates(x, y);
        Chapter chapter = new Chapter(chapterName, chapterWorld);
        SpaceMarineRaw spaceMarineRaw = new SpaceMarineRaw(
                name,
                coordinates,
                LocalDateTime.now(),
                health,
                achievements,
                weaponType,
                meleeWeapon,
                chapter
        );
        askManager.setInteractiveMode(true);
        return spaceMarineRaw;
    }
}
