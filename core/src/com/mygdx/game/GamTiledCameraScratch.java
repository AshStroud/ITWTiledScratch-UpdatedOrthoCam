package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

//Sources: Orthographic Camera Properties: http://www.gamefromscratch.com/post/2014/04/16/LibGDX-Tutorial-11-Tiled-Maps-Part-1-Simple-Orthogonal-Maps.aspx
          //Translating Orthographic Camera: https://github.com/libgdx/libgdx/wiki/Orthographic-camera

public class GamTiledCameraScratch extends ApplicationAdapter {
	//SpriteBatch batch;
	//Texture img;
	private static final int nCols = 4;
	private static final int nRows = 4;

	int nWidth;
	int nHeight;

	SpriteBatch sbBatch;
	Texture txSprite;
	//Texture BackGround;
	TextureRegion[] artrFrames;
	TextureRegion trCurrentFrame;
	float fSpriteX = 50;
	float fSpriteY = 50;
	float fSpriteSpeed = 100f;
	float fTime = 0f;
	Animation aniMain;

	boolean bBoundsCheck = false;

	//TiledMap GameMap= new TmxMapLoader().load("IntoTheWoodsRPGMap.tmx");
	TiledMap tmGameMap;
	OrthogonalTiledMapRenderer orthotmrRenderer;
	OrthographicCamera ocMainCam;

	ArrayList<Rectangle> arlRectObjectBounds = new ArrayList<Rectangle>();
	Rectangle rectSprite;
	String sDirection;

	RectangleMapObject rmoCollisionRect;
	MapObjects moCollisionDetection;
	Rectangle rectObjectBounds;
	
	@Override
	public void create () {
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		rectSprite = new Rectangle();
		sbBatch = new SpriteBatch();
		//BackGround = new Texture(Gdx.files.internal("lostwoods2.jpg"));
		txSprite = new Texture(Gdx.files.internal("CinderellaSpriteSheet.png"));
		TextureRegion[][] tmp = TextureRegion.split(txSprite, txSprite.getWidth() / nCols, txSprite.getHeight() / nRows);
		artrFrames = new TextureRegion[nCols * nRows];
		int index = 0;
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				artrFrames[index++] = tmp[i][j];
			}
		}
		aniMain = new Animation(1f, artrFrames);

		//arlRectCollisionDetection = new ArrayList<Rectangle>();

		//Setting Up Orthographic Camera
		ocMainCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ocMainCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ocMainCam.update();

		//Setting Up TiledMap
		tmGameMap= new TmxMapLoader().load("CameraPanMap.tmx");
		orthotmrRenderer = new OrthogonalTiledMapRenderer(tmGameMap);
		//Gdx.input.setInputProcessor(this);

		System.out.println("Screen Height:" + Gdx.graphics.getHeight());
		nHeight = Gdx.graphics.getHeight();
		nWidth = Gdx.graphics.getWidth();

		//Creating Bounds for Collision Detection
		moCollisionDetection = tmGameMap.getLayers().get("Map Bounds").getObjects();
		for (int i = 0; i < moCollisionDetection.getCount(); i++) {
			rmoCollisionRect = (RectangleMapObject) moCollisionDetection.get(i);
			rectObjectBounds = rmoCollisionRect.getRectangle();

			arlRectObjectBounds.add(rectObjectBounds);
			System.out.println("Rectangle Added!");
		}
		//System.out.prniln("Scr")
	}

	@Override
	public void render () {
		//Rendering Sprite
		if (fTime < 4) {
			fTime += Gdx.graphics.getDeltaTime();
		} else {
			fTime = 0;
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		trCurrentFrame = aniMain.getKeyFrame(0);
		rectSprite.set(fSpriteX, fSpriteY, trCurrentFrame.getRegionWidth(), trCurrentFrame.getRegionHeight());
		bBoundsCheck=isbBoundsCheck();
		System.out.println(bBoundsCheck);
		if(bBoundsCheck==false) {
			if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
				fSpriteX -= Gdx.graphics.getDeltaTime() * fSpriteSpeed;
				trCurrentFrame = aniMain.getKeyFrame(4 + fTime);
				sDirection = "Left";
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
				fSpriteX += Gdx.graphics.getDeltaTime() * fSpriteSpeed;
				trCurrentFrame = aniMain.getKeyFrame(8 + fTime);
				sDirection = "Right";
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
				fSpriteY += Gdx.graphics.getDeltaTime() * fSpriteSpeed;
				System.out.println("Player Sprite Y:" + fSpriteY);
				trCurrentFrame = aniMain.getKeyFrame(12 + fTime);
				sDirection = "Up";
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
				fSpriteY -= Gdx.graphics.getDeltaTime() * fSpriteSpeed;
				trCurrentFrame = aniMain.getKeyFrame(0 + fTime);
				sDirection = "Down";
			}
		}
		else{
//			if(sDirection=="Right") {
				fSpriteX += 1;
//			}
//			if(sDirection=="Left") {
//				fSpriteX -= 1;
//			}
//			if(sDirection=="Up") {
//				fSpriteY -= 1;
//			}
//			if(sDirection=="Down") {
//				fSpriteY += 1;
//			}
		}



		//Rendering Tiled Map
		ocMainCam.update();
		orthotmrRenderer.setView(ocMainCam);
		orthotmrRenderer.render();


		//Draw Sprites
		sbBatch.begin();
		//batch.draw(BackGround, 0, 0);
		sbBatch.draw(trCurrentFrame, (int) fSpriteX, (int) fSpriteY);
		sbBatch.end();

			if (fSpriteY >= nHeight) {
				ocMainCam.translate(0, nHeight, 0);
				fSpriteY = 0;
			} else if (fSpriteY < 0) {
				ocMainCam.translate(0, 0 - nHeight, 0);
				fSpriteY = nHeight;
			} else if (fSpriteX >= nWidth) {
				ocMainCam.translate(nWidth, 0, 0);
				fSpriteX = 0;
			} else if (fSpriteX < 0) {
				ocMainCam.translate(0 - nWidth, 0, 0);

				fSpriteX = nWidth;
		}

	}
	public boolean isbBoundsCheck(){
		for (int i = 0; i < arlRectObjectBounds.size(); i++) {
			if(rectSprite.overlaps(arlRectObjectBounds.get(i))) {
				System.out.println("Collision Detected");
				bBoundsCheck=true;
				return bBoundsCheck;
			}
		}
		bBoundsCheck=false;
		return bBoundsCheck;
	}
}
