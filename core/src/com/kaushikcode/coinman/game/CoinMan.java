package com.kaushikcode.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture [] man;
	int img=0;
	int slow=0;
	float gravity=0.2f;
	float velocity=0;
	int manY;
	BitmapFont font;
	int score=0;
	Rectangle manRectangle;
	int gameState=0;

	Texture dizzy;
	Random random;
//  coin
	ArrayList<Integer> coinXs=new ArrayList<>();
	ArrayList<Integer> coinYs=new ArrayList<>();
	ArrayList<Rectangle> coinRectangle=new ArrayList<>();
	int coinCount;
	Texture coin;
// bomb
	ArrayList<Integer> bombXs=new ArrayList<>();
	ArrayList<Integer> bombYs=new ArrayList<>();
	ArrayList<Rectangle> bombRectangle=new ArrayList<>();
	int bombCount;
	Texture bomb;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		dizzy=new Texture("dizzy-1.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2;
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		random=new Random();
		manRectangle=new Rectangle();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().scale(10);
	}
	public  void makeCoin(){
	float height=random.nextFloat()*Gdx.graphics.getHeight();
	coinYs.add((int)height);
	coinXs.add(Gdx.graphics.getWidth());
	}

	public  void makebomb(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
       batch.begin();
       batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
       if(gameState==1){
//       	GAME IS LIVE
		   // bomb
		   if(bombCount<250){
			   bombCount++;
		   }else{
			   bombCount=0;
			   makebomb();
		   }bombRectangle.clear();
		   for(int i=0;i<bombXs.size();i++){
			   batch.draw(bomb,bombXs.get(i),bombYs.get(i));
			   bombXs.set(i,bombXs.get(i)-8);
			   bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
		   }
//		coin
		   if(coinCount<100){
			   coinCount++;
		   }else{
			   coinCount=0;
			   makeCoin();
		   }
		   coinRectangle.clear();
		   for(int i=0;i<coinXs.size();i++){
			   batch.draw(coin,coinXs.get(i),coinYs.get(i));
			   coinXs.set(i,coinXs.get(i)-4);
			   coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
		   }


		   if(Gdx.input.justTouched()){
			   velocity=-10;
		   }
		   if(slow<8){
			   slow++;
		   }else{
			   slow=0;
			   if(img<3){
				   img++;
			   }else{
				   img=0;
			   }}
		   velocity+=gravity;
		   manY-=velocity;
		   if(manY<=0){
			   manY=0;
		   }
	   }else if(gameState==0){
//       	game is waiting to start
		   if(Gdx.input.justTouched()){
		   	gameState=1;
		   }
	   }else  if(gameState==2){
//       	game over
		   if(Gdx.input.justTouched()){
		   gameState=1;
		   manY=Gdx.graphics.getHeight()/2;
		   score=0;
		   coinXs.clear();
		   coinYs.clear();
		   coinCount=0;
		   coinRectangle.clear();
		   bombXs.clear();
		   bombYs.clear();
		   bombCount=0;
		   bombRectangle.clear();

	   }}

       if(gameState==2){
		   batch.draw(dizzy,Gdx.graphics.getWidth()-Gdx.graphics.getWidth()+man[img].getWidth()/2,manY);
	   }else {
        batch.draw(man[img],Gdx.graphics.getWidth()-Gdx.graphics.getWidth()+man[img].getWidth()/2,manY);}
       manRectangle=new Rectangle(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()+man[img].getWidth()/2,manY,man[img].getWidth(),man[img].getHeight());
       for(int i=0;i<coinRectangle.size();i++){
       	if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
       		score++;
       		coinXs.remove(i);
       		coinYs.remove(i);
       		break;
		}
	   }  for(int i=0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				gameState=2;
			}
		}
       font.draw(batch,String.valueOf(score),100,200);
       batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
