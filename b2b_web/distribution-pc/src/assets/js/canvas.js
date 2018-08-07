/*
 * @Author: litian
 * @Date: 2018/03/07
 * @Last Modified by: litian
 * @Last Modified time: 2018/04/07
 */
const BOX_PADDING = 10
const ICON_HEIGHT = 20 // 图标宽高
const POSX = 620
const POSY = 118
var hasTouch = null
const ROTATE_ICON =
    'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAABttJREFUaAXtWWlsVFUU/t7MtJ1OOy0tbemClrYsKYjKFjaJGoPyw+CSEOMSCMIffmk0GgO4YQiSkBj9YSAxjRiDJkQiJSYaxBApEgKCQdkKbUEKrV2gpZ2ZbjPPc97M7VvmbdNBA0m/ZOa+d+85955zz13OOQ8Yx/gMpDUDUlrcWuaTTbMRlVZCkmcCcjkglUPmkiBJN6iOflTK0jl45XrMr/lTyz7W5/QUYKFjeBUyniEBq1ITQmqBhP3woC4dZcamwMmm+xGVP6KZfYVm2ZOa4AZqSYpRH1/DK71LivxtaHV8TU2Bs3Imws1baMDXadazHHtPiUAaIGt8ikD1e5glDbllda/AqbZiDIf30VJ5xG3nY6OTGpAReB5zyzrd8LtT4PfmBzFCGw9ypZtO06eRrsJHB8K86jNOfTkroAgfO0od5Tp1dofb++HzLHVSwl6B+LI54XbmS3xeLM/PxuJcPwq8HuT7PBiKybgwMIwLkSH8cjuC1uFoCnqSJTICC+yWk7UCvGH7mw+5WfPL87LxfkUBlpDgkmTdZUyWcbA3gp0dvfi+J+xSEdoTudVPWG1s69GON20n4d+2G2WGPwO7phTjUVIgVfxECmy40omWoRFnVo+0HQtq3jEjNFdAOedxkRTwmzFx3VKa7R9nlCGXlorAIC2Xhr4IDtJSuTI4jN5oDEFqr/VnKsuKl5dXY6F+an/2UjsOEb096Ij1YobZPeEzZeRLCrAUnnleK80fFZ7X+a6O29h64xb+GTFb4yFlmPIML94snaDwsiKs/A/Ty7DqcjsO2C4pmsgoWKY1Skeav2QLxN2DP5xu2FWFOairKsFZ2pzrWzrxF5VuMSeQiX3TSjElK0NhCZMl5p1tVTa7ZR98Y3vwsNHtUO0vOBXfxtk92HszhMJTLVh07npKwvMwp8NDWEZ8jQmlA2SJPTWTkJk8nUIqWs0kE8tmQLICimNmoNK8+sn0gmlY1jSk+MjHKa//SCymcM7JycL64jz7XkxkE7LEGfnSMvEqczwS1hYF0VBbjsiCahyprUCQ6tLFebofNl27OdrNG7Q/9AKNNiUeyONVZFTr9fTR2Eq1CZhJx+QXVcVonzMFddUlWBqMH5dLgn7sovo7gZ20+bsSl1sNjccnlS0MMuoVgFQrmGuyfDg2azLWkVm1R6Vof3FikEweFK9jLiN0ue3u6hvlfzwxSaMVSQ+qjNxkUCARQVFDIbkF2mUSopPCiM8qi/BAdqaxOuX3w3R3CCxzVECVkXkMClAYmMCJ0CBeaurAjrYeLKIjLpTYbNzMfg0j2+NRlpjyksbfaRpLoJIsbw9VRqbTKyBi2EQP397sx1vXunGcBsijo07gBbp4hEVmkQWchhR8VmWfxroBp8PBIKMqlVXvifow3bYCV4eiWHGxDftIwXUtHXDhzQhW01LrAPo0roYpsaFSP3mcPZDl6QYa5fU2zRLvC0YFuQQN/QNouDygvKf7N51OH4Fm8qFsoWQ4VAqDBTj1YQ7eEwLOG01QuivnBtTw+ryjS6KX0aAA52/McURzUjxXkGNONMbal4vUYO83sqw99DIaFJDPWzHX3wpTJiW+D56aEMDsO3B88ljcj7Aoe7XfdPdbiRCv58SYBnoFPNJ+TZvu8SoFHntp0wrwHaBnFi3uS3ZGOCASqO8JoWsk+b4R7UrJWT0N9DIo6T7KmFlg240eSk7ErfAYRWGbywssKN1Vb6EwdDG5JQwOhja1qn6ReQ8kmyElqVeAuTjdZ4EztME+uK4O8uHkQmwsm2BBbV/NfJsrCkeJOBhqJOfOFiayJSvAuUoOHizwMVnh5141IN9630R8N3USONpyg1KiO0DBDPMJ1N8KYRspYIt4QFNnpDH3iY9f3k2Eq43E4j2bLhuOqFbQZhbg23RPdx++7OzDSTpytZcbByrzyd9fTS75Gvr5yQUR4Ml4urENg+o9KZqM5VdYOHWNsdJcAQ7qY3KjXf6Thfrk/iJsKMlLSqUM03q+RBcSh4ocbVWRf8N+kxYy7aUd7T3YTOt+yFF466DeXAEeyUVahcmW5GbhczpJHtJcRlxvh2N9A9jY2o3DVLpCymkV7jWFxBaTL6QlspbigyfzA6jM9MFj8Gku0gHwKwnMvv9Rx8uKexQYa2KL+VNMLYoheY9MI//GS/ZlJ7CLUi3dTue7YNaV6aQWRUd3eXJXv7OE0NqSU9ycJQbNxv8GGstFZprFsd7ERmHv0g8czhYQivAXE84S84kgQfWtRXvaJX9ior55DJdfZ3hI9xbQCnjPfuTTKsHP9+xnVqMi/M6nlZJ04ryNzYduUMzh9dQ7fToyG2K8bnwG/oMZ+BctNHK8WKYUDwAAAABJRU5ErkJggg=='
const DEL_ICON =
    'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAABNNJREFUaAXtWU9oHUUc/mbfSxprbGlJUk2rSRMoWG39jwe9ea9Q8OZBLFIQUU/iJQXbU4/iQVQsIp6FRjx600OhtaCoB81L/UMMjWgpr6EmeW/9fZvMe7OzM7uzu2lqIT9IdmZn5vf7vtn5+z1g27Z7oFYPqFqtzcYX546go45BxYeBeBxQ44j5FFNqQd7Jnzxj9SMa8SyenP7ebF41XY8AQXfxMmI8LwAPlgOh5qFwHhHO1SFTjcDFuQfQic9Iz74ovRyVA27VVqorPj5DQ80Ikd+s0sJsOQI/xINYbp2WgG9Kr+8o9F6qgropX+Nd7Jw6hYfUSmjTcALf/jmK1eXPZag8G+q8Wj31NQZ2Hsfj9y2FtA8jcKl1FGsy8RBPhDitX0f9iqYsCE9MfVfkq5hAAr77jTgaLnK2yeVtNKNnikjkT0AOm6Tntxw8+2I4iU0MOeYnwAm7Pua3aNi4UMqQJQZi8ZifQLt1xjdh2Whfs+FxWf71SDNC09tMFg2ufB5zE+A6D7zuasMGlx8+gIXHJvDexIjsRfXs1PgeLDw6idYjE7g78njr4g2sY8oEcxPgJoV4KFNbXtw70MCRuwYRKYXX9u3GB5OjlUmc3r8H7xzYiwEBfv+OJg4NDbhCyjvBkmDKFmcJ8HjAHdZjC6sdfLh0vVf6ytiuSiQIfmb/3p6f2X9u4PJyzv5FTMRmWZZAcrbJPx68euUvfFKDhA3+y2s38MIvixY0K8sjC7FZliWQHMysWla2K/kT80uVSLjAH/95ESuxFcSVdWBLE+CmFXiqrEKiFviEkJx4E4x9dmkCne6xflFxqgyJ+uA38FgY0wSgHiyGna4RQmLTwCeh0xit/WPjBpXGWJjTJFjxpdFdSX2uTrTF1bXUasMJGzzmEw/2vzRGi4BcA+WgX8XySGh/9cHTEzH2LT2E9B22X14qpUmYS6x2sDngxZuFMU1AR6vxJInfV9YyHhZXOlit9nEzvswXaQKJemAWl0/bE1Z7OFFxx9bte08LY5pAIn30qpZO2OA5bD6tsWO7AVCe6Zs9ianf9EtLpFzgudqsiTsOK3t1OnllqWIkakx9swjEP/WLwlM+8Pp4wGMHbVNIUBgzLD2EInXeKAtKFoGnE9fqVPUUm6h6BrLsDeLCXCv0PBQC3oglsg/w8cHR3pdg2UdXryN8OKl5PD09Zfs087JPiNwXYGXB02XtL+HAlh5CjEKtknJfjvEaaF5GymxSPhLvT47kRJQiYiI2y7IEqBpTq/TYmFzmZ+Q2pa0MeN3GReLk2G4c9l4ppSUxORTtLAFGodCq8K8OaD6vdTq4KtdK2hdyDax6MNMkzm3sE+1OVw5+637NeOtp0U2JyWHZSawrXZg7K7Tf0lnzeY9cwg8NDeLSspOjWTUofVREgj/k+PG3kHBapM7iqem3XWV+AhST2q2vhMQtFnNdsMx3IvYOTz3nU6zdQ4jtKXFTJYYIrbfNJDYx5MjtfgIETYmbKjHQvg0cRNyV2AUyez4BoqbETZV4S78E5fViZZrw/HOApab9T3/gKP4CmgQ/JScTVwTPEqurVnvyJybxzRgFw8b0H/4FzFZ37I98Jgmm79ifWW0izFMxS0Qn6jY5P3RD7hyNaLbopyNXiO132z1wC3rgP+34Hkili2z3AAAAAElFTkSuQmCC'
const SCALE_ICON =
    'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAAXNSR0IArs4c6QAABR1JREFUaAXtWU1vG0UYfmZtx4mJixrkqklpoE5LCzRFfB34UAXiQ5yCxIETJ9QfgBBQLu2h5RLgAhckED2gcgXSOydACJQiQQWhleKokZqkNC0hNWlix17eZ9317o73y/YmpVJeyfLszsw7zzMf78w8C2zZVg901QOqq9ruypPTo6ipMSjzAcAcAtQQTP6LKTUn7+Qn/6b6AynzDB4bOeeu3mm6OwIEXcfrMPGyANzTHgg1A4UJGDjVDZnOCExOD6NmnpSefU162WgPuFZaqbr4OI2UOiZEZrXcyMf2CPxu9mCldEIafEN6PRvpva0CalVG4yPkisfxoKrErRqfwC/zBVRXvpKp8nRc552VU98jk3sFjwxeiVM/HoGzpUNYl4UH8544Trsvoy4iLQHh0eJvUb6iCVjg6z+Io/4oZwnnl5E2nooiEb4AOW2snt908OyLfqttYgixYAJcsI05v0nTxg+lTFliIJYACyZQLp1MesEW0gZeHbgDh/O9GMqkZBuIYxI0GPkCzN+HFedxXgj0BtTr6PXU6G4c6HM680a9junVKr5YLOODhaUQnxJiU9jvt0/4jwA3qYTBE91O6XW39RkGDuayeH/4Loy6iLnLNNLSkRam1pxWAjwecIfdABu7sIDTi9cxdaOCqml6WjiUc0bGk2E/EBOxadZKwDrbdHk80BqxH78rr+LLq9dRzGaQUd7Zy3ehxiMLsWnWSsA6mGmlEnp86c4+fLNvEFmjAf5yZb3peSSbbqYDEz7YvAS4abV9qgxszpOhg59Zq+LN2avNMnuiRsAqKSdeC2OzGry0a/UxJyu5lB/4Z6bmwChUqZvokRG5VqvHa7CBsXnE8BKAul9GIJ6jmKUY+7/et7M5bdjzBD97c/o8/+ccnpR94dSV5ZgeidExjcDNG5ST33Vqb28GvRIuaTp4vuPC5i++eTFqBOQamPAI/Fhew9uzixjuyWB8/m9cqtbiY/UtSYyOeQnYd1gnP5HUhwv/JOLHcqJh9EahBJp5Vubz+O4B7I0TFhNozzsCVA9M875O/R4p5PHpvQXZyBUeliPCi+fnO3UVXM9SOJxsbQQofYQbo8pnAvLEru1yhXXMDZ5v7SjjlEgq5cXoHQHqNhGL+OjgdhzZsc1Cc/bfNUwsrUAHPylR5S3XJpUU9IYfYnRMI2BOOVn+KfcucVAOYAU5YdrThjUI/gWZOktxNyb/ZoLfUhhzmXsWAIaacOX5JkuyEdl2ON+3ueDZMFU9l3kJWHKfmnHltyTdBB6SMzwXLG3De95qRbBpkqSXAAtR7gux0qpzgszLgl5er+Hcyho++WtZNittRob46SjLB1srAWqVlPsC7GKlKmJBYyXk5IiwLZ3CqITMz4s78KtcGd/bNRBQs8vXxERsmrUS4BBRqwywimD/OGRnfSKfsOJo4yAmbfowy3stsgvzUl83L0hEDURzt0QfXtBH5BzPAxsvJByR45eu4WcJr8la8KXenwBb/2l6XPaEd5IF0qE3Q43j8ZF3/WoHE6CYVC59KyQ2WMz1g+V+J2Jvf/G5IMW6dQ3YdSlxUyWGCK23zKRtYgiR24MJEDQlbqrEQPkWcBBxV9qOkNnDCRA1JW6qxJs6EpTXo5VpwgteA8x12//0A0f0CNgkOJRcTIwICknHSWmFn5jEN9uImDY2JP7HHwF3rdv2I5+bBNO37WdWnQifqZhZopOlLYlyEPChG3LnSBlnoj4d+TWx9W6rBzagB/4Dn/+/3t0mPz0AAAAASUVORK5CYII='
export class Stage {
    constructor (props) {
        this.canvas = props.canvas;
        this.wcanvas = props.wcanvas
        this.fontData = props.fontData;
        this.ctx = this.canvas.getContext('2d');
        this.spriteList = [];
        const pos = this.canvas.getBoundingClientRect();
        this.canvasOffsetLeft = pos.x;
        this.canvasOffsetTop = pos.y;
        this.dragSpriteTarget = null;
        this.scaleSpriteTarget = null;
        this.rotateSpriteTarget = null;

        this.dragStartX = undefined;
        this.dragStartY = undefined;
        this.scaleStartX = undefined;
        this.scaleStartY = undefined;
        this.rotateStartX = undefined;
        this.rotateStartY = undefined;
        this.img = null;
        this.isDown = false

        this.initEvent();
    }

    getspriteList () {
      return this.spriteList
    }
    append (sprite) {
      this.spriteList.push(sprite)
      // this.spriteList.forEach(item => {
      //   if (item.id !== sprite.id) {
      //     item.ischeck = false
      //   }
      // })
      this.drawSprite()
    }

    drawSprite () {
      this.clearStage();
      this.spriteList.forEach(item => {
        // item.drawReac(this.ctx)
        // item.drawText(this.ctx)
        if (item.type === 2) {
          item.drawText(this.ctx)
        } else if (item.type === 1) {
          item.draw(this.ctx)
        } else if (item.type === 3) {
          item.drawReac(this.ctx)
        }
      });
    }

    initEvent () {
    //   hasTouch = 'ontouchstart' in window
    //   let STA_EN = hasTouch ? 'touchstart' : 'mousedown'
    //   let MV_EV = hasTouch ? 'touchmove' : 'mousemove'
    //   let END_EV = hasTouch ? 'touchend' : 'mouseup'
      this.wcanvas.addEventListener('mousedown', (e) => {
        this.handleTouchStart(e)
      });
      this.wcanvas.addEventListener('mouseup', (e) => {
          this.handleTouchEnd(e)
      });
      this.wcanvas.addEventListener('mousemove', e => {
          this.handleTouchMove(e)
          e.preventDefault()
      }, { passive: false })
    }

    handleTouchStart (e) {
      this.isDown = true
        let touchEvent = this.normalizeTouchEvent(e)
        if (!touchEvent) {
            return
        }
        let target = null

        // 触摸在sprite上，可以拖动
        if (this.getTouchSpriteTarget(touchEvent)) {
          target = this.getTouchSpriteTarget(touchEvent)
          this.initDragEvent(target, touchEvent)
          this.spriteList.forEach(item => {
            if (item.id === target.id) {
              item.ischeck = true
              if (item.type === 2) {
                item.isflag = true
                this.fontData.activeTab.value = 'tabtext'
                this.fontData.font.value = item.fontFamily
                this.fontData.textarea.value = item.fillText
                this.fontData.color.value = item.fillStyle
              } else {
                this.fontData.activeTab.value = 'tabpic'
                this.fontData.font.value = ''
                this.fontData.textarea.value = ''
                this.fontData.color.value = '#ff0000'
              }
            }
            if (item.id !== target.id && item.ischeck) {
              item.ischeck = false
              item.isflag = false
            }
          })
          this.drawSprite()
        }

        // 缩放
        if (this.getTouchTargetOfSprite(touchEvent, 'scaleIcon')) {
          target = this.getTouchTargetOfSprite(touchEvent, 'scaleIcon')
          this.initScaleEvent(target, touchEvent)
          return
        }

        // 旋转
        if (this.getTouchTargetOfSprite(touchEvent, 'rotateIcon')) {
          target = this.getTouchTargetOfSprite(touchEvent, 'rotateIcon')
          this.initRotateEvent(target, touchEvent)
          return
        }

      // 删除
      if (this.getTouchTargetOfSprite(touchEvent, 'delIcon')) {
        target = this.getTouchTargetOfSprite(touchEvent, 'delIcon')
        // this.remove(target)
      }
    }

    handleTouchMove (e) {
      if (this.isDown) {
        let touchEvent
        if (!hasTouch || e.touches.length === 1) {
          touchEvent = this.normalizeTouchEvent(e)
        } else if (e.touches.length === 2) {
          // let now = ev.touches
        }
        if (!touchEvent) {
          return;
        }
        let { touchX, touchY } = touchEvent;
        // 拖拽
        if (this.dragSpriteTarget) {
          // 重新计算坐标
          this.reCalSpritePos(this.dragSpriteTarget, touchX, touchY);
          this.drawSprite();
          return;
        }

        // 缩放
        if (this.scaleSpriteTarget) {
          this.reCalSpriteSize(this.scaleSpriteTarget, touchX, touchY);
          this.drawSprite();
          return;
        }

        // 旋转
        if (this.rotateSpriteTarget) {
          this.reCalSpriteRotate(this.rotateSpriteTarget, touchX, touchY);
          this.drawSprite();
        }
      }
    }

    handleTouchEnd (e) {
        this.isDown = false
        let touchEvent = this.normalizeTouchEvent(e)
        if (!touchEvent) {
          return
        }
        let target = null
        // 删除
        if (this.getTouchTargetOfSprite(touchEvent, 'delIcon')) {
          target = this.getTouchTargetOfSprite(touchEvent, 'delIcon')
          this.remove(target)
          return
        }

        if (this.rotateSpriteTarget) {
            this.rotateSpriteTarget.updateCoordinateByRotate();
        }

        if (this.scaleSpriteTarget) {
            this.scaleSpriteTarget.updateCoordinateByScale();
        }
        this.scaleSpriteTarget = null;
        this.dragSpriteTarget = null;
        this.rotateSpriteTarget = null;
    }

    // 初始化sprite的拖拽事件
    initDragEvent (sprite, { touchX, touchY }) {
        this.dragSpriteTarget = sprite;
        this.dragStartX = touchX;
        this.dragStartY = touchY;
    }

    // 初始化sprite的缩放事件
    initScaleEvent (sprite, { touchX, touchY }) {
        this.scaleSpriteTarget = sprite;
        this.scaleStartX = touchX;
        this.scaleStartY = touchY;
    }

    // 初始化sprite的旋转事件
    initRotateEvent (sprite, { touchX, touchY }) {
        this.rotateSpriteTarget = sprite;
        this.rotateStartX = touchX;
        this.rotateStartY = touchY;
    }

    // 通过触摸的坐标重新计算sprite的坐标
    reCalSpritePos (sprite, touchX, touchY) {
        // const [oX, oY] = sprite.pos;
        let dirX = touchX - this.dragStartX;
        let dirY = touchY - this.dragStartY;
        sprite.resetPos(dirX, dirY);
        this.dragStartX = touchX;
        this.dragStartY = touchY;
    }

    // 通过触摸的【横】坐标重新计算sprite的大小
    reCalSpriteSize (sprite, touchX, touchY) {
        // 使用X轴方向作为缩放比例的判断标准
      const [centerX, centerY] = sprite.center;
      const startVector = [this.scaleStartX - centerX, this.scaleStartY - centerY];
      const endVector = [touchX - centerX, touchY - centerY];
      const dirVector = [touchX - this.scaleStartX, touchY - this.scaleStartY];
      if (false) {
        const startVectorLength = Math.sqrt(Math.pow(startVector[0], 2) + Math.pow(startVector[1],2));
        const endVectorLength = Math.sqrt(Math.pow(endVector[0], 2) + Math.pow(endVector[1],2));
        const dirX = Math.abs(dirVector[0]);
        const dirY = Math.abs(dirVector[1]);
        let dir = dirX > dirY ? dirX : dirY;
        if (endVectorLength < startVectorLength) {
          dir = -dir;
        }
        sprite.resetSizet(dir);
      } else {
        sprite.resetSize(startVector, endVector, dirVector);
      }
        // sprite.resetSize(startVector, endVector, dirVector);
        this.scaleStartX = touchX;
        this.scaleStartY = touchY;
    }

    // 重新计算sprite的角度
    reCalSpriteRotate (sprite, touchX, touchY) {
        let [centerX, centerY] = sprite.center;
        let x1 = this.rotateStartX - centerX;
        let y1 = this.rotateStartY - centerY;
        let x2 = touchX - centerX;
        let y2 = touchY - centerY;

        // 因为sin函数
        let numerator = x1 * y2 - y1 * x2;
        let denominator = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2)) * Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
        let sin = numerator / denominator;
        let angleDir = Math.asin(sin);

        sprite.setRotateAngle(angleDir);
        this.rotateStartX = touchX;
        this.rotateStartY = touchY;
    }

    // 返回当前touch的sprite
    getTouchSpriteTarget ({ touchX, touchY }) {
        return this.spriteList.reduce((sum, sprite) => { // 这里一直循环到最后，保证每一次移动的都是最后插入的sprite
            if (this.checkIfTouchIn({ touchX, touchY }, sprite)) {
                sum = sprite;
            }
            return sum;
        }, null);
    }

    // 判断是否touch在了sprite中的某一部分上，返回这个sprite
    getTouchTargetOfSprite ({ touchX, touchY }, part) {
        return this.spriteList.reduce((sum, sprite) => {
            if (this.checkIfTouchIn({ touchX, touchY }, sprite[part])) {
                sum = sprite;
            }
            return sum;
        }, null);
    }

    // 返回点击坐标
    normalizeTouchEvent (e) {
        if (e === null || e.length === 0) return
        // let pos = this.canvas.getBoundingClientRect()
        let touchX = e.pageX - POSX
        let touchY = e.pageY - POSY
        // let touches = [].slice.call(e.touches);
        // if (touches.length > 1) { // 多点触摸，不做处理
        //     return;
        // }
        // let target = touches[0];
        // let touchX = target.pageX - this.canvasOffsetLeft;
        // let touchY = target.pageY - this.canvasOffsetTop;
        return {
            touchX,
            touchY
        }
    }

    // 判断是否在在某个sprite中移动。当前默认所有的sprite都是长方形的。
    checkIfTouchIn ({ touchX, touchY }, sprite) {
        if (!sprite) {
            return false
        }
        let [[x1, y1], [x2, y2], [x3, y3], [x4, y4]] = sprite.coordinate
        let v1 = [x1 - touchX, y1 - touchY]
        let v2 = [x2 - touchX, y2 - touchY]
        let v3 = [x3 - touchX, y3 - touchY]
        let v4 = [x4 - touchX, y4 - touchY]
        if (
            (v1[0] * v2[1] - v2[0] * v1[1]) > 0 &&
            (v2[0] * v4[1] - v4[0] * v2[1]) > 0 &&
            (v4[0] * v3[1] - v3[0] * v4[1]) > 0 &&
            (v3[0] * v1[1] - v1[0] * v3[1]) > 0
        ) {
            return true
        }
        return false
    }

    // 从场景中删除
    remove (sprite) {
        this.spriteList = this.spriteList.filter(item => {
          if (item.type === 2) {
            this.fontData.font.value = ''
            this.fontData.textarea.value = ''
            this.fontData.color.value = '#ff0000'
          }
          return item.id !== sprite.id;
        });
        this.drawSprite();
    }

    clearStage () {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}
export class Sprite {
    constructor (props) {
        this.id = Date.now() + Math.floor(Math.random() * 10);
        this.pos = props.pos;
        this.size = props.size;
        this.baseSize = props.size;
        this.minSize = props.minSize;
        this.maxSize = props.maxSize;
        this.fontSize = props.fontSize;
        this.fontFamily = props.fontFamily;
        this.fillStyle = props.fillStyle;
        this.fillText = props.fillText;
        this.strokeStyle = props.strokeStyle;
        this.url = props.url;
        this.center = [props.pos[0] + props.size[0] / 2, props.pos[1] + props.size[1] / 2];
        this.ischeck = true; // 是否选中
        this.isflag = props.isflag; // 选中字体
        this.delIcon = {}; // 删除图标
        this.scaleIcon = {}; // 缩放图标
        this.rotateIcon = {}; // 旋转图标
        this.type = props.type; // 添加类型，1==图片 2==文字
        // 图标位置
        this.coordinate = this.setCoordinate(this.pos, this.size);

        this.rotateAngle = props.rotateAngle; // 一共旋转的角度
        this.rotateAngleDir = 0; // 每次旋转角度差值

        this.scalePercent = 1; // 一共缩放的比例
        this.parent = this;

        this.init();
    }

    setCoordinate (pos, size) {
        return [
            [pos[0], pos[1]],
            [pos[0] + size[0], pos[1]],
            [pos[0], pos[1] + size[1]],
            [pos[0] + size[0], pos[1] + size[1]]
        ];
    }

    setIconCoordinate (point) {
        return [
            [point[0] - ICON_HEIGHT / 2, point[1] - ICON_HEIGHT / 2],
            [point[0] + ICON_HEIGHT / 2, point[1] - ICON_HEIGHT / 2],
            [point[0] - ICON_HEIGHT / 2, point[1] + ICON_HEIGHT / 2],
            [point[0] + ICON_HEIGHT / 2, point[1] + ICON_HEIGHT / 2]
        ];
    }

    updateCoordinateByRotate () {
        let angle = this.rotateAngleDir;
        this.updateItemCoordinateByRotate(this, this.center, angle);
        this.updateItemCoordinateByRotate(this.delIcon, this.center, angle);
        this.updateItemCoordinateByRotate(this.scaleIcon, this.center, angle);
        this.updateItemCoordinateByRotate(this.rotateIcon, this.center, angle);
        this.rotateAngleDir = 0;
    }

    updateItemCoordinateByScale (sprite, center, scale) {
        let [centerX, centerY] = center;
        let coordinateVector = sprite.coordinate.map(point => {
            return [point[0] - centerX, point[1] - centerY];
        });
        let newCoordinateVector = coordinateVector.map(vector => {
            let [x, y] = vector;
            let newX = x * scale;
            let newY = y * scale;
            return [newX, newY];
        });
        sprite.coordinate = newCoordinateVector.map(vector => {
            return [vector[0] + centerX, vector[1] + centerY];
        });
    }

    getIconCenter (iconCoordinate) {
        let point1 = iconCoordinate[0];
        let point4 = iconCoordinate[3];
        let x = (point1[0] + point4[0]) / 2;
        let y = (point1[1] + point4[1]) / 2;
        return [x, y];
    }

    getIconCoordinateByIconCenter (center) {
        let [x, y] = center;
        return [
            [x - ICON_HEIGHT / 2, y - ICON_HEIGHT / 2],
            [x + ICON_HEIGHT / 2, y - ICON_HEIGHT / 2],
            [x - ICON_HEIGHT / 2, y + ICON_HEIGHT / 2],
            [x + ICON_HEIGHT / 2, y + ICON_HEIGHT / 2]
        ];
    }

    // 更新sprite和图标的范围坐标
    updateCoordinateByScale () {
        // 缩放比
        let scale = this.size[0] / this.baseSize[0];
        // 左上角旋转按钮
        let [rotateCenterX, rotateCenterY] = this.getIconCenter(this.rotateIcon.coordinate); // 初始旋转坐标中心位
        let rotateVector = [rotateCenterX - this.center[0], rotateCenterY - this.center[1]];
        let rotateVectorNew = [rotateVector[0] * scale, rotateVector[1] * scale];
        let rotateIconCenter = [rotateVectorNew[0] + this.center[0], rotateVectorNew[1] + this.center[1]];
        this.rotateIcon.coordinate = this.getIconCoordinateByIconCenter(rotateIconCenter);
        // this.rotateIcon.coordinate = this.setCoordinate(this.rotateIcon.pos, this.rotateIcon.size)


        // 右上角缩放按钮
        let [scaleCenterX, scaleCenterY] = this.getIconCenter(this.scaleIcon.coordinate);// 初始缩放坐标中心位
        let scaleVector = [scaleCenterX - this.center[0], scaleCenterY - this.center[1]];
        let scaleVectorNew = [scaleVector[0] * scale, scaleVector[1] * scale];
        let scaleIconCenter = [scaleVectorNew[0] + this.center[0], scaleVectorNew[1] + this.center[1]];
        this.scaleIcon.coordinate = this.getIconCoordinateByIconCenter(scaleIconCenter);
        // this.scaleIcon.coordinate = this.setCoordinate(this.scaleIcon.pos, this.scaleIcon.size)


        // 左下角删除按钮
        let [delCenterX, delCenterY] = this.getIconCenter(this.delIcon.coordinate);// 初始删除坐标中心位
        let delVector = [delCenterX - this.center[0], delCenterY - this.center[1]];
        let delVectorNew = [delVector[0] * scale, delVector[1] * scale];
        let delIconCenter = [delVectorNew[0] + this.center[0], delVectorNew[1] + this.center[1]];
        this.delIcon.coordinate = this.getIconCoordinateByIconCenter(delIconCenter);
        // this.delIcon.coordinate = this.setCoordinate(this.delIcon.pos, this.delIcon.size)
        this.updateItemCoordinateByScale(this, this.center, scale);
        this.baseSize = this.size.slice(0);
    }

    updateItemCoordinateByRotate (target, center, angle) {
        let [centerX, centerY] = center;
        let coordinateVector = target.coordinate.map(point => {
          return [point[0] - centerX, point[1] - centerY];
        })
        let newCoordinateVector = coordinateVector.map(vector => {
        let [x, y] = vector;
        // x2 = x1 * cos - y1 * sin;
        // y2 = x1 * sin + y1 * cos;
        let newX = x * Math.cos(angle) - y * Math.sin(angle);
        let newY = x * Math.sin(angle) + y * Math.cos(angle);
        return [newX, newY];
      })
      target.coordinate = newCoordinateVector.map(vector => {
        return [vector[0] + centerX, vector[1] + centerY];
      })
    }
    drawReac (ctx) {
      let sprite = this;
      ctx.save();
      let [x, y] = sprite.pos;
      let [width, height] = sprite.size;
      ctx.beginPath();
      if (this.rotateAngle !== 0) {
        let centerX = x + width / 2;
        let centerY = y + height / 2;
        ctx.translate(centerX, centerY);
        ctx.rotate(this.rotateAngle);
        ctx.translate(-centerX, -centerY);
      }

      ctx.rect(x, y, width, height);
      ctx.fillStyle = '#f00';
      ctx.fill();
      ctx.restore();
      this.drawIcon(ctx, sprite.delIcon)
      this.drawIcon(ctx, sprite.rotateIcon)
      this.drawIcon(ctx, sprite.scaleIcon)
    }
    draw (ctx) {
      let sprite = this
      ctx.save()
      let [x, y] = sprite.pos
      let [width, height] = sprite.size
      ctx.beginPath()
      if (this.rotateAngle !== 0) {
        let centerX = x + width / 2
        let centerY = y + height / 2
        ctx.translate(centerX, centerY)
        ctx.rotate(this.rotateAngle)
        ctx.translate(-centerX, -centerY)
      }
      if (this.img === undefined || this.img === null || this.img === '') {
        Promise.all([
          this.loadImage(sprite.url)
        ]).then(imgs => {
          this.img = imgs[0]
          ctx.drawImage(this.img, x, y, width, height)
        })
      } else {
        ctx.drawImage(this.img, x, y, width, height)
      }
      ctx.restore()
      if (sprite.ischeck) {
        this.drawDash(ctx)
        this.drawIcon(ctx, sprite.delIcon)
        this.drawIcon(ctx, sprite.rotateIcon)
        this.drawIcon(ctx, sprite.scaleIcon)
      }
  }
    renderText (ctx) {
      let sprite = this
      ctx.save()
      let [x, y] = sprite.pos
      let [width, height] = sprite.size
    //   let letterSpaceing = 5
    //   for (let i = 0; i < sprite.fillText.length; i++) {
    //     const str = sprite.fillText.slice(i, i + 1).toString()
    //     if (str.match(/[A-Za-z0-9]/) && (y < 576)) {
    //         ctx.save()
    //         ctx.translate(x, y)
    //         ctx.font = sprite.fontSize + 'px ' + sprite.fontFamily
    //         ctx.textBaseline = 'bottom'
    //         ctx.fillText(str, 0, 0)
    //         ctx.fillStyle = sprite.fillStyle
    //         ctx.restore()
    //         y += ctx.measureText(str).width + letterSpaceing // 计算文字宽度
    //     } else if (str.match(/[\u4E00-\u9FA5]/) && (y < 576)) {
    //         ctx.save()
    //         ctx.textBaseline = 'top'
    //         ctx.fillStyle = 'red'
    //         ctx.fillText(str, x, y)
    //         ctx.font = ' 16px Microsoft Yahei'
    //         ctx.restore()
    //         y += ctx.measureText(str).width + letterSpaceing // 计算文字宽度
    //     }
    //   }
      ctx.beginPath()
      ctx.font = sprite.fontSize + 'px ' + sprite.fontFamily
      ctx.fillStyle = sprite.fillStyle
      ctx.fillText(sprite.fillText, x, y + height / 1.2)
      ctx.globalCompositeOperation = 'source-over'
      ctx.restore()
      this.drawDash(ctx)
      this.drawIcon(ctx, sprite.delIcon)
      this.drawIcon(ctx, sprite.rotateIcon)
      this.drawIcon(ctx, sprite.scaleIcon)
    }
    drawText (ctx) {
        let sprite = this
        ctx.save()
        let [x, y] = sprite.pos
        let [width, height] = sprite.size
        ctx.beginPath()
        if (this.rotateAngle !== 0) {
            let centerX = x + width / 2
            let centerY = y + height / 2
            ctx.translate(centerX, centerY)
            ctx.rotate(this.rotateAngle)
            ctx.translate(-centerX, -centerY)
        }
        ctx.font = sprite.fontSize + 'px ' + sprite.fontFamily
        ctx.fillStyle = sprite.fillStyle
        ctx.fillText(sprite.fillText, x, y + height / 1.1)
        ctx.globalCompositeOperation = 'source-over'
        ctx.restore()
        if (sprite.ischeck) {
          this.drawDash(ctx)
          this.drawIcon(ctx, sprite.delIcon)
          this.drawIcon(ctx, sprite.rotateIcon)
          this.drawIcon(ctx, sprite.scaleIcon)
        }
    }
    // 加载图片
    loadImage (url) {
      return new Promise(resolve => {
        let img = new Image()
        img.crossOrigin = '*'
        img.onload = () => resolve(img)
        img.src = url
      })
    }
    drawDash (ctx) {
      let sprite = this
      ctx.save()
      let [x, y] = sprite.pos
      let [width, height] = sprite.size
      ctx.setLineDash([2, 5])
      if (this.rotateAngle !== 0) {
        let centerX = x + width / 2;
        let centerY = y + height / 2;
        ctx.translate(centerX, centerY);
        ctx.rotate(this.rotateAngle);
        ctx.translate(-centerX, -centerY);
      }
      ctx.strokeStyle = '#999999'
      ctx.strokeRect(x - 2, y - 2, width + 4, height + 4)
      ctx.restore()
    }
    drawIcon (ctx, icon) {
        ctx.beginPath();
        ctx.save();
        let [x, y] = icon.pos;
        let [width, height] = icon.size;
        if (this.rotateAngle !== 0) {
            let [spriteX, spriteY] = this.pos;
            let [spriteW, spriteH] = this.size;
            let centerX = spriteX + spriteW / 2;
            let centerY = spriteY + spriteH / 2;
            ctx.translate(centerX, centerY);
            ctx.rotate(this.rotateAngle);
            ctx.translate(-centerX, -centerY);
        }
        if (icon.self) {
            ctx.drawImage(icon.self, x, y, width, height);
        } else {
            let img = new Image();
            img.crossOrigin = 'anonymous';
            img.src = icon.url;
            img.onload = function () {
                icon.self = img;
                ctx.drawImage(img, x, y, width, height);
            }
        }
        ctx.restore();
    }
    init () {
        this.initDelIcon();
        this.initRotateIcon();
        this.initScaleIcon();
    }

    // 删除按钮，左下角
    initDelIcon () {
        let [width, height] = this.size;
        let [x, y] = this.pos;
        this.delIcon = {
            ...this.delIcon,
            pos: [x - ICON_HEIGHT, y + height],
            size: [ICON_HEIGHT, ICON_HEIGHT],
            url: DEL_ICON,
            parent: this
        };
        this.delIcon.coordinate = this.setCoordinate(this.delIcon.pos, this.delIcon.size);
    }

    // 缩放按钮，右上角
    initScaleIcon () {
        let [width, height] = this.size;
        let [x, y] = this.pos;
        this.scaleIcon = {
            ...this.scaleIcon,
            pos: [x + width, y - ICON_HEIGHT],
            size: [ICON_HEIGHT, ICON_HEIGHT],
            url: SCALE_ICON,
            parent: this
        };

        this.scaleIcon.coordinate = this.setCoordinate(this.scaleIcon.pos, this.scaleIcon.size);
    }

    // 旋转按钮，左上角
    initRotateIcon () {
        let [x, y] = this.pos;
        this.rotateIcon = {
            ...this.rotateIcon,
            pos: [x - ICON_HEIGHT, y - ICON_HEIGHT],
            size: [ICON_HEIGHT, ICON_HEIGHT],
            url: ROTATE_ICON,
            parent: this
        };
        this.rotateIcon.coordinate = this.setCoordinate(this.rotateIcon.pos, this.rotateIcon.size);
    }

    // 重置icon的位置与大小
    resetIconPos () {
        let [width, height] = this.size;
        let [x, y] = this.pos;
        this.rotateIcon = {
            ...this.rotateIcon,
            pos: [x - ICON_HEIGHT, y - ICON_HEIGHT],
            size: [ICON_HEIGHT, ICON_HEIGHT]
        };
        this.scaleIcon = {
            ...this.scaleIcon,
            pos: [x + width, y - ICON_HEIGHT],
            size: [ICON_HEIGHT, ICON_HEIGHT]
        };
        this.delIcon = {
            ...this.delIcon,
            pos: [x - ICON_HEIGHT, y + height],
            size: [ICON_HEIGHT, ICON_HEIGHT]
        };
    }

    resetPos (dirX, dirY) {
        let [oX, oY] = this.pos;
        this.pos = [oX + dirX, oY + dirY];
        this.center = [this.center[0] + dirX, this.center[1] + dirY];

        // 更新四个顶点的坐标
        this.coordinate = this.coordinate.map(point => {
            return [point[0] + dirX, point[1] + dirY];
        });

        if (this.delIcon) {
            let [x, y] = this.delIcon.pos;
            this.delIcon.pos = [x + dirX, y + dirY];
            this.delIcon.coordinate = this.delIcon.coordinate.map(point => {
                return [point[0] + dirX, point[1] + dirY];
            });
        }
        if (this.scaleIcon) {
            let [x, y] = this.scaleIcon.pos;
            this.scaleIcon.pos = [x + dirX, y + dirY];
            this.scaleIcon.coordinate = this.scaleIcon.coordinate.map(point => {
                return [point[0] + dirX, point[1] + dirY];
            });
        }
        if (this.rotateIcon) {
            let [x, y] = this.rotateIcon.pos;
            this.rotateIcon.pos = [x + dirX, y + dirY];
            this.rotateIcon.coordinate = this.rotateIcon.coordinate.map(point => {
                return [point[0] + dirX, point[1] + dirY];
            });
        }
    }

    resetSizet (dir) {
      const sprite = this;
      const [oWidth, oHeight] = sprite.size;
      this.scalePercent = (oWidth + dir) / oWidth; // 使用x轴的长度来确定缩放的比例
      const [minWidth, minHeight] = sprite.minSize;
      const [maxWidth, maxHeight] = sprite.maxSize;
      const [centerX, centerY] = sprite.center;

      let width = oWidth * this.scalePercent;
      let height = oHeight * this.scalePercent;
      width < minWidth && (width = minWidth);
      height < minHeight && (height = minHeight);
      width > maxWidth && (width = maxWidth);
      height > maxHeight && (height = maxHeight);
      // if (sprite.type === 2) {
      //   sprite.fontSize = sprite.fontSize * this.scalePercent
      // }
      const x = centerX - width / 2;
      const y = centerY - height / 2;
      sprite.pos = [x, y];
      sprite.size = [width, height];
      sprite.fontSize = height
      sprite.resetIconPos();
    }

    resetSize (startVector, endVector, dirVector) {
        let sprite = this;
        let [oWidth, oHeight] = sprite.size;
        let [minWidth, minHeight] = sprite.minSize
        let [maxWidth, maxHeight] = sprite.maxSize
        let minScale = minWidth / oWidth < minHeight / oHeight ? minHeight / oHeight : minWidth / oWidth
        let maxScale = maxWidth / oWidth > maxHeight / oHeight ? maxHeight / oHeight : maxWidth / oWidth
        let startVectorLength = Math.sqrt(Math.pow(startVector[0], 2) + Math.pow(startVector[1], 2));
        let endVectorLength = Math.sqrt(Math.pow(endVector[0], 2) + Math.pow(endVector[1], 2));
        let dir = Math.abs(dirVector[0]) > Math.abs(dirVector[1]) ? Math.abs(dirVector[0]) : Math.abs(dirVector[1]);
        if (endVectorLength < startVectorLength) {
          dir = -dir
        }
        this.scalePercent = Math.abs(dirVector[0]) > Math.abs(dirVector[1]) ? Math.abs((oWidth + dir)) / oWidth : Math.abs((oHeight + dir)) / oHeight
        this.scalePercent = this.scalePercent < minScale ? minScale : this.scalePercent > maxScale ? maxScale : this.scalePercent
        // console.log(this.scalePercent)
        // let startVectorLength = Math.sqrt(Math.pow(startVector[0], 2) + Math.pow(startVector[1], 2));
        // let endVectorLength = Math.sqrt(Math.pow(endVector[0], 2) + Math.pow(endVector[1], 2));
        // let dirX = Math.abs(dirVector[0]);
        // let dirY = Math.abs(dirVector[1]);
        // let dir = dirX > dirY ? dirX : dirY;
        // if (endVectorLength < startVectorLength) {
        //   dir = -dir;
        // }

        // dirX > dirY ? dirX : dirY;
        // this.scalePercent = dirX > dirY ? Math.abs((oWidth + dir)) / oWidth : Math.abs((oHeight + dir)) / oHeight
        // this.scalePercent = (oWidth + dir) / oWidth; // 使用x轴的长度来确定缩放的比例
        // console.log(this.scalePercent)
        let [centerX, centerY] = sprite.center;

        let width = oWidth * this.scalePercent;
        let height = oHeight * this.scalePercent;
        // let moveX = (width - oWidth) / 2
        // let moveY = (height - oHeight) / 2

        // width < minWidth && (width = minWidth);
        // height < minHeight && (height = minHeight);
        // width > maxWidth && (width = maxWidth);
        // height > maxHeight && (height = maxHeight);

        let x = centerX - width / 2;
        let y = centerY - height / 2;
        sprite.pos = [x, y];
        sprite.size = [width, height];
        if (sprite.type === 2) {
          sprite.fontSize = height
        }
        sprite.resetIconPos();
    }

    setRotateAngle (angleDir) {
        this.rotateAngle += angleDir;
        this.rotateAngleDir += angleDir;
    }
}
