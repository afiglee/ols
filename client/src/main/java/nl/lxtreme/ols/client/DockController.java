/*
 * OpenBench LogicSniffer / SUMP project 
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 *
 * Copyright (C) 2006-2010 Michael Poppitz, www.sump.org
 * Copyright (C) 2010-2012 J.W. Janssen, www.lxtreme.nl
 */
package nl.lxtreme.ols.client;


import java.awt.*;
import java.util.*;

import javax.swing.*;

import nl.lxtreme.ols.client.signaldisplay.*;
import nl.lxtreme.ols.client.signaldisplay.laf.*;
import nl.lxtreme.ols.client.signaldisplay.view.*;

import org.noos.xing.mydoggy.*;
import org.noos.xing.mydoggy.plaf.*;
import org.noos.xing.mydoggy.plaf.ui.content.*;


/**
 * Provides a simple controller for the dock windows.
 */
public class DockController
{
  // CONSTANTS

  public static final String TW_ACQUISITION = AcquisitionDetailsView.ID;
  public static final String TW_MEASURE = MeasurementView.ID;
  public static final String TW_CURSORS = CursorDetailsView.ID;

  public static final String GROUP_DEFAULT = "Default";

  // VARIABLES

  private final MyDoggyToolWindowManager windowManager;

  // CONSTRUCTORS

  /**
   * Creates a new {@link DockController} instance.
   */
  public DockController()
  {
    final MyDoggyToolWindowManager wm = new MyDoggyToolWindowManager( Locale.getDefault(),
        MyDoggyToolWindowManager.class.getClassLoader() );
    wm.setDockableMainContentMode( false );

    final ContentManager contentManager = wm.getContentManager();
    contentManager.setContentManagerUI( new MyDoggyMultiSplitContentManagerUI() );

    this.windowManager = wm;
  }

  // METHODS

  /**
   * @param aWindow
   */
  private static void tweakToolWindow( final ToolWindow aWindow )
  {
    RepresentativeAnchorDescriptor<?> anchorDesc = aWindow.getRepresentativeAnchorDescriptor();
    anchorDesc.setTitle( aWindow.getTitle() );
    anchorDesc.setPreviewEnabled( false );
    if ( aWindow.getIcon() != null )
    {
      anchorDesc.setIcon( aWindow.getIcon() );
    }

    final ToolWindowType[] types = ToolWindowType.values();
    for ( ToolWindowType type : types )
    {
      ToolWindowTypeDescriptor desc = aWindow.getTypeDescriptor( type );
      desc.setAnimating( false );
      desc.setAutoHide( false );
      desc.setEnabled( true );
      desc.setHideRepresentativeButtonOnVisible( false );
      desc.setIdVisibleOnTitleBar( false );
      desc.setTitleBarButtonsVisible( false );
      desc.setTitleBarVisible( false );
    }

    DockedTypeDescriptor desc = ( DockedTypeDescriptor )aWindow.getTypeDescriptor( ToolWindowType.DOCKED );
    desc.setPopupMenuEnabled( false );

    aWindow.setAvailable( true );
    aWindow.setHideOnZeroTabs( false );
    aWindow.setVisible( UIManager.getBoolean( UIManagerKeys.SHOW_TOOL_WINDOWS_DEFAULT ) );
  }

  /**
   * @return
   */
  public Component get()
  {
    return this.windowManager;
  }

  /**
   * @param aToolWindow
   * @param aGroupName
   */
  public void registerToolWindow( final IToolWindow aToolWindow, final String aGroupName )
  {
    ToolWindow tw = this.windowManager.registerToolWindow( aToolWindow.getId(), aToolWindow.getName(),
        aToolWindow.getIcon(), ( Component )aToolWindow, ToolWindowAnchor.RIGHT );

    final ToolWindowGroup group = this.windowManager.getToolWindowGroup( aGroupName );
    group.setImplicit( false );
    group.addToolWindow( tw );

    tweakToolWindow( tw );
  }

  /**
   * @param aComponent
   */
  public void setMainContent( final Component aComponent )
  {
    this.windowManager.setMainContent( aComponent );
  }
}
