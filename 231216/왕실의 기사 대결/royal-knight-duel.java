import java.io.*;
import java.util.*;
public class Main {
    static int L, N, Q;

    static int[][] map, zone;
    static List<int[]> knight;
    static int[] dr = new int[] {-1, 0, 1, 0};
    static int[] dc = new int[] {0, 1, 0, -1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        map = new int[L+2][L+2]; //체스판
        zone = new int[L+2][L+2]; //기사들 위치 표시
        for(int i=0 ; i<L+2 ; i++){
            map[0][i] = 2;
            map[i][L+1] = 2;
            map[i][0] = 2;
            map[L+1][i] = 2;
            zone[0][i] = -1;
            zone[i][L+1] = -1;
            zone[i][0] = -1;
            zone[L+1][i] = -1;
        }
        for(int i=1 ; i<=L ; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=1 ; j<=L ; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        knight = new ArrayList<>(); //기사 정보
        for(int n=0 ; n<N ; n++){
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            int[] night = new int[] {r, c, h, w, k, 0};
            //좌측상단행, 열, 세로길이, 가로길이, 체력, 피해
            knight.add(night);
            for(int i=r ; i<r+h ; i++){
                for(int j=c ; j<c+w ; j++){
                    zone[i][j] = n+1;
                }
            }
        }

        //여기서부터 움직이기 시작
        for(int q=0 ; q<Q ; q++){
            // System.out.println("지시"+" "+q);
            st = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(st.nextToken()); //기사 번호
            int direction = Integer.parseInt(st.nextToken());
            //0위, 1오, 2아래, 3왼
            // System.out.println("죽어랏");
            boolean goKill = move(idx, idx, direction);
            // if(goKill) kill(idx);

            //확인용
            // for(int i=0 ; i<L+2 ; i++){
            //     for(int j=0 ; j<L+2 ; j++){
            //         System.out.print(zone[i][j]);
            //     }
            //     System.out.println();
            // }
        }
        int result = 0;
        for(int i=0 ; i<knight.size() ; i++){
            if(knight.get(i)[0]==-1) continue;
            // System.out.println(knight.get(i)[0]+" "+knight.get(i)[4]+" "+knight.get(i)[5]);
            result += knight.get(i)[5];
        }
        System.out.println(result);

    } //main

    static boolean move(int command, int idx, int dir){
        //live명령을 받은 기사, idx움직일기사
        boolean go = true;
        int r=knight.get(idx-1)[0];
        if(r == -1) return false;
        int c=knight.get(idx-1)[1];
        int h=knight.get(idx-1)[2];
        int w=knight.get(idx-1)[3];
        int k=knight.get(idx-1)[4];
        int attack=knight.get(idx-1)[5];

        //주변 기사 확인
        for(int i=r ; i<r+h ; i++){
            for(int j=c ; j<c+w ; j++){
                int tmp = zone[i+dr[dir]][j+dc[dir]];
                if(tmp==idx) continue;
                else if(tmp==-1 || map[i+dr[dir]][j+dc[dir]]==2) return false;
                else if(tmp!=0){
                    go = move(command, tmp, dir);
                }
            }
        }

        //기사 옮기기
        if(go && command!=idx){
            knight.set(idx-1, new int[] {r+dr[dir], c+dc[dir], h, w, k, attack});
            for(int i=r ; i<r+h ; i++){
                for(int j=c ; j<c+w ; j++){
                    if(i<r+dr[dir] || i>=r+h+dr[dir] || j<c+dc[dir] || j>=c+w+dc[dir])
                        zone[i][j] = 0;
                    zone[i+dr[dir]][j+dc[dir]] = idx;
                }
            }
            kill(idx);
        }

        return go;
    } //move


    static void kill(int idx){
        // for(int i=0 ; i<knight.size() ; i++){
            // if(idx-1==i) continue;
            int r = knight.get(idx-1)[0];
            // if(r==-1) continue;
            int c = knight.get(idx-1)[1];
            int h = knight.get(idx-1)[2];
            int w = knight.get(idx-1)[3];
            int k = knight.get(idx-1)[4];
            int attack = knight.get(idx-1)[5];
            
            for(int ii=r ; ii<r+h ; ii++){
                for(int jj=c ; jj<c+w ; jj++){
                    if(map[ii][jj]==1){
                        attack++;
                    }
                }
            }
            // System.out.println(r+" "+attack);
            knight.set(idx-1, new int[]{r, c, h, w, k, attack});
            for(int ii=r ; ii<r+h ; ii++){
                for(int jj=c ; jj<c+w ; jj++){
                    if(k<=attack) {
                        knight.set(idx-1, new int[]{-1});
                        zone[ii][jj]=0;
                    }        
                }
            }

        // }
    }//kill
}