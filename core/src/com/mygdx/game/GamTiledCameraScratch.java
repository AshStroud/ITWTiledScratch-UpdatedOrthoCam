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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

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
	float fSpriteX = 0;
	float fSpriteY = 0;
	float fSpriteSpeed = 100f;
	float fTime = 0f;
	Animation aniMain;

	//TiledMap GameMap= new TmxMapLoader().load("IntoTheWoodsRPGMap.tmx");
	TiledMap tmGameMap;
	OrthogonalTiledMapRenderer orthotmrRenderer;
	OrthographicCamera ocMainCam;
	
	@Override
	public void create () {
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
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
		//System.out.prniln("Scr")
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
		//Rendering Sprite
		if (fTime < 4) {
			fTime += Gdx.graphics.getDeltaTime();
		} else {
			fTime = 0;
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		trCurrentFrame = aniMain.getKeyFrame(0);

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			fSpriteX -= Gdx.graphics.getDeltaTime() * fSpriteSpeed;
			trCurrentFrame = aniMain.getKeyFrame(4 + fTime);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			fSpriteX += Gdx.graphics.getDeltaTime() * fSpriteSpeed;
			trCurrentFrame = aniMain.getKeyFrame(8 + fTime);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
			fSpriteY += Gdx.graphics.getDeltaTime() * fSpriteSpeed;
			System.out.println("Player Sprite Y:" + fSpriteY);
			trCurrentFrame = aniMain.getKeyFrame(12 + fTime);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
			fSpriteY -= Gdx.graphics.getDeltaTime() * fSpriteSpeed;
			trCurrentFrame = aniMain.getKeyFrame(0 + fTime);
		}

		//Rendering Tiled Map
		ocMainCam.update();
		orthotmrRenderer.setView(ocMainCam);
		orthotmrRenderer.render();

		//OrthoGraphic Camera
		//ocMainCam.position.set(fSpriteX, fSpriteY, 0);
		//sbBatch.setProjectionMatrix(ocMainCam.combined);
		//ocMainCam.update();

		//Draw Sprites
		sbBatch.begin();
		//batch.draw(BackGround, 0, 0);
		sbBatch.draw(trCurrentFrame, (int) fSpriteX, (int) fSpriteY);
		sbBatch.end();

		if(fSpriteY >= nHeight){
			ocMainCam.translate(0, nHeight, 0);
			//nHeight += Gdx.graphics.getHeight();
			fSpriteY = 0;
		}
		else if(fSpriteY < 0){
			ocMainCam.translate(0, 0-nHeight, 0);
			fSpriteY = nHeight;
		}
		else if(fSpriteX >= nWidth){
			ocMainCam.translate(nWidth, 0, 0);
			//nHeight += Gdx.graphics.getHeight();
			fSpriteX = 0;
		}
		else if(fSpriteX < 0){
			ocMainCam.translate(0-nWidth, 0, 0);
			//nHeight += Gdx.graphics.getHeight();
			fSpriteX = nWidth;
		}
	}
}
