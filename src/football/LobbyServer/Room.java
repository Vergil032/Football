/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.LobbyServer;

import java.util.ArrayList;

/**
 *
 * @author nicknacck
 */
public class Room {

    private final String name;
    private final ArrayList<LobbyPlayer> teamRed = new ArrayList<>();
    private final ArrayList<LobbyPlayer> teamBlue = new ArrayList<>();
    private final int playerPerTeam;
    private final int winvalue;
    private final LobbyPlayer admin;
    private final int wincondition;
    private final LobbyServer lobby;
    private final String password;

    public Room(String name, int playerPerTeam, int wincondition, int winvalue, LobbyPlayer admin, LobbyServer lobby, String password) {
        this.name = name;
        this.playerPerTeam = playerPerTeam;
        this.wincondition = wincondition;
        this.winvalue = winvalue;
        this.admin = admin;
        this.lobby = lobby;
        this.password = password;
    }

    public int join(LobbyPlayer p, String pass) {
        if (!password.equals(pass)) {
            return -1;
        }
        if (isFull()) {
            return -1;
        }

        if (teamBlue.size() < teamRed.size()) {
            teamBlue.add(p);
        } else {
            teamRed.add(p);
        }
        p.setRoom(this);
        p.send("JOIN]" + name);
        updateView();

        return 0;
    }

    public void leave(LobbyPlayer p) {
        if (p == null) {
            return;
        }

        if (p == admin) {
            lobby.destroyRoom(this);
        } else {
            teamBlue.remove(p);
            teamRed.remove(p);
            updateView();
        }
    }

    public void changeTeam(LobbyPlayer p) {
        if (p != null) {
            if (teamBlue.contains(p)) {
                teamBlue.remove(p);
                teamRed.add(p);
                updateView();
            } else if (teamRed.contains(p)) {
                teamRed.remove(p);
                teamBlue.add(p);
                updateView();
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isFull() {
        return teamBlue.size() + teamRed.size() >= playerPerTeam * 2;
    }

    public int getAmountOfPlayer() {
        return teamBlue.size() + teamRed.size();
    }

    public String getName() {
        return name;
    }

    private String roomToJSON() {
        return "{\"room\": {\"teamRed\":" + teamToJSON(teamRed) + ", \"teamBlue\":" + teamToJSON(teamBlue) + "}}";
    }

    private String teamToJSON(ArrayList<LobbyPlayer> team) {
        String json = "[";
        for (int i = 0; i < team.size(); i++) {
            json += "{\"name\": \"" + team.get(i).name + "\"}";
            if (i < team.size() - 1) {
                json += ",";
            }
        }
        json += "]";
        return json;
    }

    private void updateView() {
        String json = roomToJSON();
        for (int i = 0; i < teamBlue.size(); i++) {
            teamBlue.get(i).send(json);
        }
        for (int i = 0; i < teamRed.size(); i++) {
            teamRed.get(i).send(json);
        }
    }

    public void leaveAll() {
        for (int i = 0; i < teamBlue.size(); i++) {
            teamBlue.get(i).send("LEAVE]");
        }
        for (int i = 0; i < teamRed.size(); i++) {
            teamRed.get(i).send("LEAVE]");
        }
    }

    public int getWincondition() {
        return wincondition;
    }

    public int getWinvalue() {
        return winvalue;
    }

    public int getPlayerPerTeam() {
        return playerPerTeam;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<LobbyPlayer> getTeamBlue() {
        return teamBlue;
    }

    public ArrayList<LobbyPlayer> getTeamRed() {
        return teamRed;
    }

    public LobbyPlayer getAdmin() {
        return admin;
    }

    public void start() {
        if (canStart()) {
            sendToAll("START]");
        }
    }
    
    public boolean canStart(){
        return teamBlue.size() == teamRed.size() && teamBlue.size() + teamRed.size() == playerPerTeam * 2;
    }

    private void sendToAll(String msg) {
        for (int i = 0; i < teamBlue.size(); i++) {
            teamBlue.get(i).send(msg);
        }
        for (int i = 0; i < teamRed.size(); i++) {
            teamRed.get(i).send(msg);
        }
    }

}
