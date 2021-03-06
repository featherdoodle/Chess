/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SyBye8898
 */
public class Board {
    
    public Piece[][][] board = new Piece[8][8][2];
    
    public Board(boolean t,int n){//t means if its ai, n is if ai is white or black        
        for(int i=0;i<2;i++){
            board[0][0][i]=Piece.ROOK;
            board[0][7][i]=Piece.ROOK;
            board[0][1][i]=Piece.KNIGHT;
            board[0][6][i]=Piece.KNIGHT;
            board[0][2][i]=Piece.BISHOP;
            board[0][5][i]=Piece.BISHOP;
            for(int j=0;j<8;j++){
                board[1][j][i]=Piece.PAWN;
            }
        }
        board[0][3][0]=Piece.KING;
        board[0][3][1]=Piece.QUEEN;
        board[0][4][1]=Piece.KING;
        board[0][4][0]=Piece.QUEEN;
    }
    public boolean[][] analyzeBoard(int p,int a,int b){
        boolean[][]r=new boolean[8][8];
        switch(board[a][b][p]){
            case Piece.PAWN://GOTTA FIX THAT WEIRD RULE FOR PAWN
                if(board[a+1][b][p]==null&&board[a+1][b][(p-1)*(p-1)]==null){
                    r[a+1][b]=true;
                }
                if(board[a+1][b+1][(p-1)*(p-1)]!=null){
                    r[a+1][b+1]=true;
                }
                if(board[a+1][b-1][(p-1)*(p-1)]!=null){
                    r[a+1][b-1]=true;
                }
                break;
            case Piece.ROOK:
                int i,j,count=0;
                for(int k=1;count<2;k*=-1){
                    i=a;j=b;
                    while(board[i][j][p]==null&&board[i][j][(p-1)*(p-1)]==null){
                        r[i+k][j]=true;
                        i+=k;
                    }
                    if(board[i][j][(p-1)*(p-1)]!=null){
                        r[i][j]=true;
                    }
                    i=a;j=b;
                    while(board[i][j][p]==null&&board[i][j][(p-1)*(p-1)]==null){
                        r[i][j+k]=true;
                        j+=k;
                    }
                    if(board[i][j][(p-1)*(p-1)]!=null){
                        r[i][j]=true;
                    }
                    count++;
                }
        }
    }
    
    public double findBoardValue(){
        
        //use number of pieces on the board to determine stage
        
        double combinationValue = 0;
        double totalPieceValues = 0;
        double totalValue = 0;
        
        int[][] pieceArray = cloneArray(findNumberPieces());
        
        if(pieceArray[5][0] == 0){
            return 1000000;
        }else if (pieceArray[5][1] == 0){
            return -1000000;
        }
        
        double value = 0;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 2; j++){
                for(int k = 0; k < pieceArray[i][j]; k++){
                    int side = j == 0? -1:+1;
                    int pieceValue = 0;
                    if(i == 0){
                        pieceValue = 1;                  
                    }else if(i == 1){
                        pieceValue = 3;
                    }else if(i == 2){
                        pieceValue = 3;
                    }else if(i == 3){
                        pieceValue = 5;
                    }else if(i == 4){
                        pieceValue = 9;
                    }
                    
                    totalPieceValues += pieceValue;
                    value += side * pieceValue;
                }
            }
        }
        
        int midSquares = 0;
        //78 total
        if(value > 62){//begin game
            
            for(int i = 3; i <= 4; i++){
                for(int j = 3; j <= 4; j++){
                    for(int a = -2; a <= 2; a += 4){
                        for(int b = -1; b <= 1; b += 2){
                            if(board[i+a][j+b][0] == Piece.KNIGHT){
                                midSquares--;
                            }if(board[i+b][j+a][0] == Piece.KNIGHT){
                                midSquares--;
                            }if(board[i+a][j+b][1] == Piece.KNIGHT){
                                midSquares++;
                            }if(board[i+b][j+a][1] == Piece.KNIGHT){
                                midSquares++;
                            }
                            
                            if(board[i+1][j][0] == Piece.PAWN){//using i as y //this also changes depending if the board array gets swapped
                                midSquares--;
                            }if(board[i-1][j][1] == Piece.PAWN){
                                midSquares++;
                            }
                            //is there a variable contianing if the pawn can move two squares? that is necessary here
                        }
                    }
                }
            }
            
            //method to check if piece is on the edge, negative value
            //not sure if should be used 'cause it runs through entire board-not worth it
            //B, R, Q, N better in open
            
            //B > R
        }else if(value > 24){//midgame
            
            //if there is a simple way to determine whether or not the position is open or closed, that would be useful
            
            //R >= B
        }else{//endgame
            //R >> B
            //N bad w/ edges and corners
            //passed pawns //player w/ pawns closest to other sidre
            //doubled pawns = weak (throughout whole game)
            if((pieceArray[3][0] == 2)&&(pieceArray[4][1] == 1)){
                value -= 1;//some value :/ //this is if black has two rooks, white has one queen
            }else if((pieceArray[3][1] == 2)&&(pieceArray[4][0] == 1)){
                value += 1;//some value :/
            }
            
            /*
            King & Rook can checkmate
            
            K+B // K+N == tie
            
            K+P = maybe checkmate
            
            K+2B can checkmate
            
            K+B+N can
            K+2N (if other is stupid)
            K == tie
            */
            
        }
        totalValue = totalPieceValues + midSquares + combinationValue;
        return totalValue;
        }
    
    public boolean onEdge(int a, int b){
        
        boolean edge = false;
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if((a+i < 0)||(a+i >= 8)||(b+i < 0)||(b+i >= 8)){
                    edge = true;
                }
            }
        }
        return edge;
    }
    
    public int[][] findNumberPieces(){
        int[][] pieceArray = new int[6][2];
        for(int p = 0; p < 2; p++){
            for(int a = 0; a < 8; a++){
                for(int b = 0; b < 8; b++){
                    if(board[a][b][p] == Piece.PAWN){
                        pieceArray[0][p]++;
                    }else if(board[a][b][p] == Piece.KNIGHT){
                        pieceArray[1][p]++;
                    }else if(board[a][b][p] == Piece.BISHOP){
                        pieceArray[2][p]++;
                    }else if(board[a][b][p] == Piece.ROOK){
                        pieceArray[3][p]++;
                    }else if(board[a][b][p] == Piece.QUEEN){
                        pieceArray[4][p]++;
                    }else if(board[a][b][p] == Piece.KING){
                        pieceArray[5][p]++;
                    }
                }
            }
        }
        return pieceArray;
    }
    
    public int[][] cloneArray(int[][] array){
        int[][] returnArray = new int[array.length][array[0].length];
        
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                returnArray[i][j] = array[i][j];
            }
        }
        return returnArray;
    }
   
}