package ru.ezhov.example

import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.awt.event.MouseEvent
import javax.swing.AbstractButton
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.SwingUtilities
import javax.swing.event.MouseInputAdapter

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ /** An application that requires no other files.  */
object GlassPaneDemo {
    private var myGlassPane: MyGlassPane? = null

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private fun createAndShowGUI() {
        //Create and set up the window.
        val frame = JFrame("GlassPaneDemo")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        //Start creating and adding components.
        val changeButton = JCheckBox("Glass pane \"visible\"")
        changeButton.isSelected = false

        //Set up the content pane, where the "main GUI" lives.
        val contentPane = frame.contentPane
        contentPane.layout = FlowLayout()
        contentPane.add(changeButton)
        contentPane.add(JButton("Button 1"))
        contentPane.add(JButton("Button 2"))

        //Set up the menu bar, which appears above the content pane.
        val menuBar = JMenuBar()
        val menu = JMenu("Menu")
        menu.add(JMenuItem("Do nothing"))
        menuBar.add(menu)
        frame.jMenuBar = menuBar

        //Set up the glass pane, which appears over both menu bar
        //and content pane and is an item listener on the change
        //button.
        myGlassPane = MyGlassPane(changeButton, menuBar,
            frame.contentPane)
        changeButton.addItemListener(myGlassPane)
        frame.glassPane = myGlassPane

        //Show the window.
        frame.pack()
        frame.isVisible = true
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater { createAndShowGUI() }
    }
}

/**
 * We have to provide our own glass pane so that it can paint.
 */
internal class MyGlassPane(aButton: AbstractButton,
                           menuBar: JMenuBar,
                           contentPane: Container?) : JComponent(), ItemListener {
    var pointPP: Point? = null

    //React to change button clicks.
    override fun itemStateChanged(e: ItemEvent) {
        isVisible = e.stateChange == ItemEvent.SELECTED
    }

    override fun paintComponent(g: Graphics) {
        if (pointPP != null) {
            g.color = Color.red
            g.fillOval(pointPP!!.x - 10, pointPP!!.y - 10, 20, 20)
        }
    }

    fun setPoint(p: Point?) {
        pointPP = p
    }

    init {
        val listener = CBListener(aButton, menuBar,
            this, contentPane)
        addMouseListener(listener)
        addMouseMotionListener(listener)
    }
}

/**
 * Listen for all events that our check box is likely to be
 * interested in.  Redispatch them to the check box.
 */
internal class CBListener(liveButton: Component, menuBar: JMenuBar,
                          glassPane: MyGlassPane, contentPane: Container?) : MouseInputAdapter() {
    var toolkit: Toolkit
    var liveButton: Component
    var menuBar: JMenuBar
    var glassPane: MyGlassPane
    var contentPane: Container?

    init {
        toolkit = Toolkit.getDefaultToolkit()
        this.liveButton = liveButton
        this.menuBar = menuBar
        this.glassPane = glassPane
        this.contentPane = contentPane
    }

    override fun mouseMoved(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseDragged(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseClicked(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseEntered(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseExited(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mousePressed(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseReleased(e: MouseEvent) {
        redispatchMouseEvent(e, true)
    }

    //A basic implementation of redispatching events.
    private fun redispatchMouseEvent(e: MouseEvent,
                                     repaint: Boolean) {
        val glassPanePoint = e.point
        val container = contentPane
        val containerPoint = SwingUtilities.convertPoint(
            glassPane,
            glassPanePoint,
            contentPane)
        if (containerPoint.y < 0) { //we're not in the content pane
            if (containerPoint.y + menuBar.height >= 0) {
                //The mouse event is over the menu bar.
                //Could handle specially.
            } else {
                //The mouse event is over non-system window
                //decorations, such as the ones provided by
                //the Java look and feel.
                //Could handle specially.
            }
        } else {
            //The mouse event is probably over the content pane.
            //Find out exactly which component it's over.
            val component = SwingUtilities.getDeepestComponentAt(
                container,
                containerPoint.x,
                containerPoint.y)
            if (component != null && component == liveButton) {
                //Forward events over the check box.
                val componentPoint = SwingUtilities.convertPoint(
                    glassPane,
                    glassPanePoint,
                    component)
                component.dispatchEvent(MouseEvent(component,
                    e.id,
                    e.getWhen(),
                    e.modifiers,
                    componentPoint.x,
                    componentPoint.y,
                    e.clickCount,
                    e.isPopupTrigger))
            }
        }

        //Update the glass pane if requested.
        if (repaint) {
            glassPane.setPoint(glassPanePoint)
            glassPane.repaint()
        }
    }
}
