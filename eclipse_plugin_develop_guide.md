1. File -> New -> Plug-in Project

2. Dialog: Plug-in Project
   * Project name: com.kissyid.buildts

3. Dialog: Content
   * ID: com.kissyid.buildts
   * Name: BuildTS
   * Vendor: Hao Pi

4. Dialog: Templates
   * Unselect `Create a plug-in using one of the templates`

5. Edite `MANIFEST.MF`
   * Overview: Select `This plug-in is a singleton`
   * Dependencies:
     - Already exists: org.eclipse.ui, org.eclipse.runtime
     - Add... -> org.eclipse.core.resources
     - Change org.eclipse.core.resources Minimum Version to 3.7.100 (This is the version of FlashBuilder 4.7)
   * Build: Binary Build -> Select `src`
   * Extensions:
     - Add... -> Select: org.eclipse.ui.startup -> class: com.kissyid.buildts.OnStartup
     - Add... -> Select: org.eclipse.ui.actionSets -> id: com.kissyid.buildts.actionSet, label: Build TS Action Set, visible: true
       - actionSet conext menu -> New -> menu
         - id: buildTSMenu
         - label: Build TS
         - menu context menu -> New -> separator -> name: buildTSGroup
       - actionSet conext menu -> New -> action
         - id: com.kissyid.buildts.SwitcherAction
         - label: Build TS
         - menubarPath: Project/buildTSGroup
         - toolbarPath: [Option] Show in toolbar, example: buildTSGroup
         - icon: [Option] You can set an icon here, example: icons/sample.gif
         - style: toggle
         - state: true
         - class: com.kissyid.buildts.SwitcherAction

6. Export Plugin
   * File -> Export... -> Deployable plug-ins and fragments
