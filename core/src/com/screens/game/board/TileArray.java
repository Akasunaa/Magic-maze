package com.screens.game.board;
import static com.screens.game.board.CaseCorrespondance.*;
import static com.utils.Colors.*;

public class TileArray {
    /*
    Une manière très moche de stocker les différentes cartes stockées sous forme de tableaux
    Ah et aussi il faut les inverser parce que why not j'ai envie de dire
     */

    public static int[][] getArray(int number) {
        int[][] answer = new int[4][4];
        int[][] temp = getArrayTemp(number);
        for (int i = 0; i < 4; i++) {
            answer[3 - i] = temp[i];
        }
        return answer;
    }

    public static int[][] getArrayWallHorizontal(int number) {
        int[][] answer = new int[3][4];
        int[][] temp = getArrayWallHorizontalTemp(number);
        for (int i = 0; i < 3; i++) {
            answer[2 - i] = temp[i];
        }
        return answer;
    }

    public static int[][] getArrayWallVertical(int number) {
        int[][] answer = new int[4][3];
        int[][] temp = getArrayWallVerticalTemp(number);
        for (int i = 0; i < 4; i++) {
            answer[3 - i] = temp[i];
        }
        return answer;
    }

    /* Et maintenant, on construit allégrement la tuile
        Je vais maintenant expliquer comment on utilise le système de tableau
        On a un tableau 4x4 pour les cases, et pour les murs...
        Un tableau de 3x4 pour ceux verticaux
        Un tableau de 4x3 pour ceux horizontaux
        Pour le mur: 1 = un mur, 0 = pas de mur
    */

    private static int[][] getArrayTemp(int number) {
        if (number == 0) {
            return new int[][]{
                    {hourglass, vanilla, exit + orange, portal + purple},
                    {exit + purple, vanilla, vanilla, portal + yellow},
                    {portal + orange, vanilla, vanilla, exit + green},
                    {portal + green, exit + yellow, vanilla, unnacessible}
            };
        }
        if (number == 1) {
            return new int[][]{
                    {hourglass, vanilla, exit+green, vanilla},
                    {exit + purple, vanilla, vanilla, vanilla},
                    {vanilla, vanilla, vanilla, exit + yellow},
                    {vanilla, exit + orange, vanilla, unnacessible}
            };
        }
        if (number == 2) {
            return new int[][]{
                    {finalExit + purple, unnacessible, unnacessible, unnacessible},
                    {vanilla, unnacessible, unnacessible, portal + purple},
                    {unnacessible, unnacessible, vanilla, entrance},
                    {unnacessible, exit + orange, vanilla, portal + green}
            };
        }
        if (number == 3) {
            return new int[][]{
                    {unnacessible, vanilla, entrance, unnacessible},
                    {exit + yellow, vanilla, vanilla, hourglass},
                    {green + portal, vanilla, vanilla, purple + exit},
                    {unnacessible, orange + portal, vanilla, vanilla}
            };
        }
        if (number == 4) {
            return new int[][]{
                    {unnacessible, orange + portal, unnacessible, unnacessible},
                    {entrance, vanilla, hourglass, unnacessible},
                    {unnacessible, vanilla, vanilla, purple + exit},
                    {unnacessible, green + exit, unnacessible, yellow + portal}
            };
        }
        if (number == 5) {
            return new int[][]{
                    {unnacessible, purple + portal, yellow + exit, vanilla},
                    {entrance, vanilla, vanilla, vanilla},
                    {vanilla, vanilla, hourglass, orange + exit},
                    {unnacessible, green + exit, vanilla, vanilla}
            };
        }
        if (number == 6) {
            return new int[][]{
                    {unnacessible, unnacessible, yellow + weapon, unnacessible},
                    {green+ exit, vanilla, vanilla, unnacessible},
                    {unnacessible, vanilla, vanilla, entrance},
                    {unnacessible, orange+ exit, unnacessible, purple + portal}
            };
        }
        if (number == 7) {
            return new int[][]{
                    {unnacessible, unnacessible, green + portal, unnacessible},
                    {vanilla, vanilla, vanilla, vanilla},
                    {orange + weapon, unnacessible, unnacessible, purple + exit},
                    {unnacessible, entrance, unnacessible, yellow + portal}
            };
        }
        if (number == 8) {
            return new int[][]{
                    {vanilla, vanilla, vanilla, vanilla},
                    {orange + exit, unnacessible, yellow + portal, vanilla},
                    {unnacessible, unnacessible, unnacessible, purple + exit},
                    {green + weapon, entrance, vanilla, vanilla}
            };
        }
        if (number == 9) {
            return new int[][]{
                    {unnacessible, vanilla, vanilla, vanilla},
                    {entrance, vanilla, unnacessible, vanilla},
                    {unnacessible, unnacessible, orange + portal, vanilla},
                    {purple + weapon, yellow + exit, vanilla, vanilla}
            };
        } else return null;
    }

    public static int[][] getArrayWallVerticalTemp(int number) {
        if (number == 0) {
            return new int[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 1},
                    {0, 0, 1}
            };
        }
        if (number == 1) {
            return new int[][]{
                    {1, 0, 0},
                    {1, 0, 1},
                    {0, 0, 1},
                    {0, 0, 1}
            };
        }
        if (number == 2) {
            return new int[][]{
                    {0, 1, 0},
                    {1, 0, 1},
                    {1, 1, 0},
                    {1, 0, 0}
            };
        }
        if (number == 3) {
            return new int[][]{
                    {1, 0, 1},
                    {0, 0, 0},
                    {0, 0, 1},
                    {1, 1, 0}
            };
        }
        if (number == 4) {
            return new int[][]{
                    {1, 1, 0},
                    {0, 0, 1},
                    {1, 0, 0},
                    {1, 1, 1}
            };
        }
        if (number == 5) {
            return new int[][]{
                    {1, 1, 0},
                    {1, 0, 0},
                    {0, 0, 1},
                    {1, 0, 0}
            };
        }
        if (number == 6) {
            return new int[][]{
                    {0, 1, 1},
                    {0, 0, 1},
                    {1, 1, 0},
                    {1, 1, 1}
            };
        }
        if (number == 7) {
            return new int[][]{
                    {0, 1, 1},
                    {0, 0, 0},
                    {1, 0, 1},
                    {1, 1, 1}
            };
        }
        if (number == 8) {
            return new int[][]{
                    {0, 0, 0},
                    {1, 1, 1},
                    {0, 0, 1},
                    {0, 0, 0}
            };
        }
        if (number == 9) {
            return new int[][]{
                    {1, 0, 0},
                    {0, 1, 1},
                    {1, 1, 1},
                    {0, 0, 0}
            };
        } else return null;
    }

    public static int[][] getArrayWallHorizontalTemp(int number) {
        if (number == 0) {
            return new int[][]{
                    {1, 0, 0, 1},
                    {1, 0, 0, 1},
                    {1, 0, 0, 1},
            };
        }
        if (number == 1) {
            return new int[][]{
                    {0, 0, 1, 0},
                    {0, 0, 0, 0},
                    {0, 1, 1, 1},
            };
        }
        if (number == 2) {
            return new int[][]{
                    {0, 1, 0, 1},
                    {1, 1, 1, 0},
                    {0, 1, 0, 1},
            };
        }
        if (number == 3) {
            return new int[][]{
                    {1, 0, 1, 1},
                    {1, 1, 0, 1},
                    {1, 0, 0, 0},
            };
        }
        if (number == 4) {
            return new int[][]{
                    {1, 0, 1, 0},
                    {1, 0, 1, 1},
                    {0, 0, 1, 0},
            };
        }
        if (number == 5) {
            return new int[][]{
                    {1, 0, 1, 0},
                    {0, 0, 1, 1},
                    {1, 0, 1, 0},
            };
        }
        if (number == 6) {
            return new int[][]{
                    {1, 1, 0, 1},
                    {1, 0, 0, 1},
                    {0, 0, 1, 0},
            };
        }
        if (number == 7) {
            return new int[][]{
                    {1, 1, 0, 1},
                    {0, 1, 1, 0},
                    {1, 0, 0, 0},
            };
        }
        if (number == 8) {
            return new int[][]{
                    {0, 1, 0, 0},
                    {1, 0, 1, 0},
                    {1, 1, 1, 0},
            };
        }
        if (number == 9) {
            return new int[][]{
                    {1, 0, 1, 0},
                    {1, 1, 1, 0},
                    {1, 1, 0, 0},
            };
        } else return null;
    }
}
