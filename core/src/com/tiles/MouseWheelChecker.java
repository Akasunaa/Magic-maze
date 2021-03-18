package com.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

import static com.utils.Functions.mouseInput;
import static com.utils.MainConstants.camera;
public class MouseWheelChecker implements InputProcessor {

    MouseWheelChecker() {}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            middleLastClick = new Vector3(mouseInput(),0);
            cameraLastPosition = camera.position.cpy();
            System.out.println(middleLastClick);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    Vector3 middleLastClick;
    Vector3 cameraLastPosition;

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            Vector3 delta = new Vector3(mouseInput(),0).sub(middleLastClick);
            camera.position.sub(delta);
            System.out.println(camera.position);

        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom = (float) Math.max(0.4,Math.min(5,camera.zoom +amountY * 0.2));
        return false;
    }
}