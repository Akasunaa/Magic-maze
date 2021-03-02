package com.tiles.pathfinding;

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
        Pour les cases:
         - 0 = innacessible
         - 1 = accessible, case normale
         - 2 = une entrée
         // les racourcis et les escalators sont rajoutés à la main
         - 7 = un sablier
         - 8 = boule de crystal
         - 9 = caméra
         - 1x = une sortie normale
         - 2x = un téléporteur (voir comment les coder plus tard)
         - 3x = une arme
         - 4x = une sortie de joueur

         pour les grands nombres, on a
         - 0 -> vert
         - 1 -> violet
         - 2 -> jaune
         - 3 -> orange
        */

    private static int[][] getArrayTemp(int number) {
        if (number == 0) {
            return new int[][]{
                    {7, 1, 13, 21},
                    {11, 1, 1, 22},
                    {23, 1, 1, 10},
                    {20, 12, 1, 0}
            };
        }
        if (number == 1) {
            return new int[][]{
                    {7, 1, 10, 1},
                    {11, 1, 1, 1},
                    {1, 1, 1, 12},
                    {1, 13, 1, 0}
            };
        }
        if (number == 2) {
            return new int[][]{
                    {41, 0, 0, 0},
                    {1, 0, 0, 21},
                    {0, 0, 1, 2},
                    {0, 13, 1, 20}
            };
        }
        if (number == 3) {
            return new int[][]{
                    {0, 1, 2, 0},
                    {12, 1, 1, 7},
                    {20, 1, 1, 11},
                    {0, 23, 1, 1}
            };
        }
        if (number == 4) {
            return new int[][]{
                    {0, 23, 0, 0},
                    {2, 1, 7, 0},
                    {0, 1, 1, 11},
                    {0, 10, 0, 22}
            };
        }
        if (number == 5) {
            return new int[][]{
                    {0, 21, 12, 1},
                    {2, 1, 1, 1},
                    {1, 1, 7, 13},
                    {0, 10, 1, 1}
            };
        }
        if (number == 6) {
            return new int[][]{
                    {0, 0, 32, 0},
                    {10, 1, 1, 0},
                    {0, 1, 1, 2},
                    {0, 13, 0, 21}
            };
        }
        if (number == 7) {
            return new int[][]{
                    {0, 0, 20, 0},
                    {1, 1, 1, 1},
                    {33, 0, 0, 11},
                    {0, 2, 0, 22}
            };
        }
        if (number == 8) {
            return new int[][]{
                    {1, 1, 1, 1},
                    {13, 0, 22, 1},
                    {0, 0, 0, 11},
                    {30, 2, 1, 1}
            };
        }
        if (number == 9) {
            return new int[][]{
                    {0, 1, 1, 1},
                    {2, 1, 0, 1},
                    {0, 0, 23, 1},
                    {31, 12, 1, 1}
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
