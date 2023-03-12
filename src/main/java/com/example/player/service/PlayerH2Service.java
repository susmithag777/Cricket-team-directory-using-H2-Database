package com.example.player.service;


import com.example.player.repository.PlayerRepository;
import com.example.player.model.Player;
import com.example.player.model.PlayerRowMapper;


import java.util.*;


import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;




@Service
public class PlayerH2Service implements PlayerRepository {


    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList <Player> getPlayers(){
        List<Player> playerList = db.query("SELECT * FROM TEAM", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
       
    }
    @Override
    public Player addPlayer(Player player) {
        db.update("insert into TEAM (playerName,jerseyNumber,role) values(?,?,?)", player.getPlayerName(),player.getJerseyNumber(),player.getRole());
		Player savedPlayer=db.queryForObject("select * from  TEAM where playerName=? and jerseyNumber=? and role=?", new PlayerRowMapper(),player.getPlayerName(),player.getJerseyNumber(),player.getRole());
        return savedPlayer;
    }
    @Override
    public Player getPlayerByPlayerId(int playerId) {

       try {
            Player player = db.queryForObject("select * from TEAM where playerid =?", new PlayerRowMapper(),playerId);
            return player;

       } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }
       
    }
    @Override
    public Player updatePlayer(int playerId, Player player) {
        if(player.getPlayerName()!=null) {
            db.update("update TEAM set playerName=? where playerId=?",player.getPlayerName(),playerId);
        }
        if(player.getJerseyNumber()!=0) {
            db.update("update TEAM set jerseyNumber=? where playerId=?",player.getJerseyNumber(),playerId);
        }
        if(player.getRole()!=null) {
            db.update("update TEAM set Role=? where playerId=?",player.getRole(),playerId);
        }
        return getPlayerByPlayerId(playerId);

    }
    @Override
    public void deletePlayer(int playerId) {
        db.update("DELETE FROM TEAM WHERE playerId=?",playerId);
       
    }
   
}
