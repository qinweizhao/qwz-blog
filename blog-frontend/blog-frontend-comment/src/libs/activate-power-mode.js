'use strict';
import getCaretCoordinates from '@/libs/textarea-caret-position.js'

var canvas = document.createElement('canvas');

canvas.id = "activate-canvas"
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
canvas.style.cssText = 'position:fixed;top:0;left:0;pointer-events:none;z-index:999999';
window.addEventListener('resize', function () {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
});

var context;
var particles = [];
var particlePointer = 0;
var rendering = false;

POWERMODE.shake = true;

function getRandom(min, max) {
    return Math.random() * (max - min) + min;
}

function getColor(el) {
    if (POWERMODE.colorful) {
        var u = getRandom(0, 360);
        return 'hsla(' + getRandom(u - 10, u + 10) + ', 100%, ' + getRandom(50, 80) + '%, ' + 1 + ')';
    } else {
        return window.getComputedStyle(el).color;
    }
}

function getCaret() {
    // 操作影子dom中的数据
    var el = document.activeElement.shadowRoot != null ? document.activeElement.shadowRoot.activeElement : document.activeElement;
    var bcr;
    if (el.tagName === 'TEXTAREA' ||
        (el.tagName === 'INPUT' && el.getAttribute('type') === 'text')) {
        var offset = getCaretCoordinates(el, el.selectionEnd);
        bcr = el.getBoundingClientRect();
        return {
            x: offset.left + bcr.left,
            y: offset.top + bcr.top,
            color: getColor(el)
        };
    }
    var selection = window.getSelection();
    if (selection.rangeCount) {
        var range = selection.getRangeAt(0);
        var startNode = range.startContainer;
        if (startNode.nodeType === document.TEXT_NODE) {
            startNode = startNode.parentNode;
        }
        bcr = range.getBoundingClientRect();
        return {
            x: bcr.left,
            y: bcr.top,
            color: getColor(startNode)
        };
    }
    return { x: 0, y: 0, color: 'transparent' };
}

function createParticle(x, y, color) {
    return {
        x: x,
        y: y,
        alpha: 1,
        color: color,
        velocity: {
            x: -1 + Math.random() * 2,
            y: -3.5 + Math.random() * 2
        }
    };
}

export default function POWERMODE() {
    var dcm = document.activeElement.shadowRoot != null ? document.activeElement.shadowRoot.ownerDocument : document;
    if(dcm.getElementById("activate-canvas") == null) {
        dcm.body.appendChild(canvas);
        context = canvas.getContext('2d');
    }
    { // spawn particles
        var caret = getCaret();
        var numParticles = 5 + Math.round(Math.random() * 10);
        while (numParticles--) {
            particles[particlePointer] = createParticle(caret.x, caret.y, caret.color);
            particlePointer = (particlePointer + 1) % 500;
        }
    }
    { // shake screen
        if (POWERMODE.shake) {
            var intensity = 1 + 2 * Math.random();
            var x = intensity * (Math.random() > 0.5 ? -1 : 1);
            var y = intensity * (Math.random() > 0.5 ? -1 : 1);
            dcm.body.style.marginLeft = x + 'px';
            dcm.body.style.marginTop = y + 'px';
            setTimeout(function() {
                dcm.body.style.marginLeft = '';
                dcm.body.style.marginTop = '';
            }, 75);
        }
    }
    if(!rendering){
        requestAnimationFrame(loop);
    }
}
POWERMODE.colorful = false;

function loop() {
    rendering = true;
    context.clearRect(0, 0, canvas.width, canvas.height);
    var rendered = false;
    var rect = canvas.getBoundingClientRect();
    for (var i = 0; i < particles.length; ++i) {
        var particle = particles[i];
        if (particle.alpha <= 0.1) continue;
        particle.velocity.y += 0.075;
        particle.x += particle.velocity.x;
        particle.y += particle.velocity.y;
        particle.alpha *= 0.96;
        context.globalAlpha = particle.alpha;
        context.fillStyle = particle.color;
        context.fillRect(
            Math.round(particle.x - 1.5) - rect.left,
            Math.round(particle.y - 1.5) - rect.top,
            3, 3
        );
        rendered = true;
    }
    if(rendered){
        requestAnimationFrame(loop);
    }else{
        rendering = false;
    }
}
