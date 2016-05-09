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
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

//Sources: Orthographic Camera Properties: http://www.gamefromscratch.com/post/2014/04/16/LibGDX-Tutorial-11-Tiled-Maps-Part-1-Simple-Orthogonal-Maps.aspx
          //Translating Orthographic Camera: https://github.com/libgdx/libgdx/wiki/Orthographic-camera

public class GamTiledCameraScratch extends ApplicationAdapter {
	private static final int nCols = 4;
	private static final int nRows = 4;

	int nHeight;
	int nWidth;

	int nTileLayerHeight;
	int nTileLayerWidth;
	SpriteBatch sbBatch;
	Texture txSprite;
	//Texture BackGround;
	TextureRegion[] artrFrames;
	TextureRegion trCurrentFrame;
	float fSpriteX = 100;
	float fSpriteY = 100;
	float fSpriteSpeed = 100f;
	float fTime = 0f;
	Animation aniMain;
	TiledMap tmGameMap;
	OrthogonalTiledMapRenderer orthotmrRenderer;
	OrthographicCamera ocMainCam;

	ArrayList<Rectangle> arlRectCollisionDetection = new ArrayList<Rectangle>();
	Rectangle rectSprite;
	String sDirection;
	Object objProperty;
	TiledMapTileLayer.Cell cCurrentCell;
	MapProperties mpWalkable;
	TiledMapTileLayer tmtlBounds;

	@Override
	public void create() {
		rectSprite = new Rectangle();
		sbBatch = new SpriteBatch();
		mpWalkable = new MapProperties();
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

		//Setting Up Orthographic Camera
		ocMainCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ocMainCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ocMainCam.update();

		//Setting Up TiledMap
		tmGameMap = new TmxMapLoader().load("CameraPanMap.tmx");
		orthotmrRenderer = new OrthogonalTiledMapRenderer(tmGameMap);

		System.out.println("Screen Height:" + Gdx.graphics.getHeight());
		nHeight = Gdx.graphics.getHeight();
		nWidth = Gdx.graphics.getWidth();

		tmtlBounds = (TiledMapTileLayer) tmGameMap.getLayers().get("Foreground");
		nTileLayerHeight = tmtlBounds.getHeight();
		nTileLayerWidth = tmtlBounds.getWidth();
		/*for (int i = 0; i < tmtlBounds.getWidth(); i++) {
			for (int j = 0; j < tmtlBounds.getHeight(); j++) {
				if (tmtlBounds.getCell(i, j) != null) {
					//tmtlBounds.getProperties().get("Walkable");
					cCurrentCell = tmtlBounds.getCell(i, j);
					objProperty = cCurrentCell.getTile().getProperties().containsKey("Blocked");
					if (objProperty != null) {
						System.out.println("Rectangle added!");
						arlRectCollisionDetection.add(new Rectangle(i * 64, j * 64, 32, 32));
					}
				}
			}
		}*/
		FindBounds();
	}

	@Override
	public void render() {
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
		//bBoundsCheck = isbBoundsCheck();
		//System.out.println(bBoundsCheck);
		//if (bBoundsCheck == false) {
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
			//System.out.println("Player Sprite Y:" + fSpriteY);
			trCurrentFrame = aniMain.getKeyFrame(12 + fTime);
			sDirection = "Up";
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
			fSpriteY -= Gdx.graphics.getDeltaTime() * fSpriteSpeed;
			trCurrentFrame = aniMain.getKeyFrame(0 + fTime);
			sDirection = "Down";
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

		if (fSpriteY > nHeight) {
			//RemoveBoundsOnScreen();
			ocMainCam.translate(0, nHeight, 0);
			fSpriteY = 0;
			//FindBoundsOnScreen();
		} else if (fSpriteY < 0) {
			RemoveBoundsOnScreen();
			ocMainCam.translate(0, 0 - nHeight, 0);
			fSpriteY = nHeight;
			//FindBoundsOnScreen();
		} else if (fSpriteX > nWidth) {
			//RemoveBoundsOnScreen();
			ocMainCam.translate(nWidth, 0, 0);
			fSpriteX = 0;
			//FindBoundsOnScreen();
		} else if (fSpriteX < 0) {
			//RemoveBoundsOnScreen();
			ocMainCam.translate(0 - nWidth, 0, 0);
			fSpriteX = nWidth;
			//FindBoundsOnScreen();
		}
		for (int i = 0; i < arlRectCollisionDetection.size(); i++) {
			if (rectSprite.overlaps(arlRectCollisionDetection.get(i))) {
				System.out.println("Collision detected!");
				System.out.println(sDirection);
				if (sDirection == "Up") {
					fSpriteY -= 10f;
				} else if (sDirection == "Down") {
					fSpriteY += 10f;
				} else if (sDirection == "Right") {
					fSpriteX -= 10f;
				} else if (sDirection == "Left") {
					fSpriteX += 10f;
				}
			}
		}
	}
	public void RemoveBoundsOnScreen() {
		for(int i = 0; i < arlRectCollisionDetection.size(); i++){
			if(arlRectCollisionDetection.get(i).getX() <= nWidth && arlRectCollisionDetection.get(i).getY() <= nHeight){
			arlRectCollisionDetection.remove(i);
			}
		}
	}
	public void FindBounds(){
		for (int i = 0; i < nTileLayerWidth; i++) {
			for (int j = 0; j < nTileLayerHeight; j++) {
				if (tmtlBounds.getCell(i, j) != null) {
					//tmtlBounds.getProperties().get("Walkable");
					cCurrentCell = tmtlBounds.getCell(i, j);
					objProperty = cCurrentCell.getTile().getProperties().containsKey("Blocked");
					if (objProperty != null) {
						System.out.println("Rectangle added!");
						arlRectCollisionDetection.add(new Rectangle(i * 64, j * 64, 32, 32));
					}
				}
			}
		}

	}
	public void FindBoundsOnScreen(){
		for (int i = 0; i < tmtlBounds.getWidth(); i++) {
			for (int j = 0; j < tmtlBounds.getHeight(); j++) {
				if (tmtlBounds.getCell(i, j) != null) {
					//tmtlBounds.getProperties().get("Walkable");
					cCurrentCell = tmtlBounds.getCell(i, j);
					objProperty = cCurrentCell.getTile().getProperties().containsKey("Blocked");
					if (objProperty != null) {
						arlRectCollisionDetection.add(new Rectangle(i * 64, j * 64, 32, 32));
					}
				}
			}
		}
	}
}
	/*public boolean isbBoundsCheck(){
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
}*/
