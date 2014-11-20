/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@OptionsPanelController.ContainerRegistration(  id = "Settings",
                                                categoryName = "#OptionsCategory_Name_Settings",
                                                iconBase = "org/desert/gui/images/NetBeansCube2.png",
                                                keywords = "#OptionsCategory_Keywords_Settings",
                                                keywordsCategory = "Settings",
                                                position = 400)
@NbBundle.Messages(value = {"OptionsCategory_Name_Settings=Settings", "OptionsCategory_Keywords_Settings=Settings"})
package org.desert.gui.optionpanel;

import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;
