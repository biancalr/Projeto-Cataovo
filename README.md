# Projeto-Cataovo

<section>
<p lang="en">Project developed as part of the Course Completion Work of the IFPE campus recife institution of the TADS course.</p>

<p lang="en">To use this project it is needed to install OpenCV locally</p>
<h5>Instructions:</h5>
<ul>
<li>Access the <a href="https://opencv.org/releases/">OpenCV website</a> in the required release (4.7.0 in this project).</li>
<li>Download and install the option corresponding to your operating system (Windows in this project)</li>
<li>Once installed, include the OpenCV library in the project in this path: (install-path)/opencv/build/java/opencv-470.jar</li>
<li>Change the "run.jvmargs" property in project.properties to identify locally installed OpenCV: (install-path)/opencv/build/java/x64 </li>
<li>The previous property can also be changed within Netbeans by following the path:
     <ul>
         <li>Right click on the project</li>
         <li>Access "Properties"</li>
         <li>When Opening, Access "Execute"</li>
         <li>Opening the option, you must change the VM Options field to: -Djava.library.path="(installation path)opencv\build\java\x64" --enable-preview</li>
         <li>Note: The OpenCV library is not prepared for special characters (like `´^~). Therefore, care should be taken when choosing where to save palette images.</li>
     </ul>
</li>
</ul>

<p lang="en">The general objective of this work is to develop a monolithic application for counting and identifying Aedes aegypti eggs contained in an image, using techniques
computer vision.</p>

<p lang="en">The project has the following specific objectives:</p>
<ul>
<li>Develop a module for counting Aedes mosquito eggs using the tool</li>
<li>Serve as a measuring parameter of the efficiency of public policies to combat the mosquito</li>
<li>Provide a database containing the worksheets and images resulting from the processing</li>
</ul>

<h4>Structuring the Application Modules</h4>
<p lang="en">The complete software consists of three modules:</p>
<ul>
<li>Gold Standard Generation Module</li>
<li>Automatic Counting Module</li>
<li>Evaluation Module</li>
</ul>
 
<div>
<h4>Gold Standard Generation Module</h4>
<section>
<p lang="en">In the Gold Standard Generation Module, a manual egg counting tool is provided. In this module, the application offers a tool which allows, for each frame that makes up a palette, the area where each egg is located individually must be manually demarcated on the screen. The amount of eggs will be the amount of demarcated regions.</p>
</section>
<img src="/images/mod_manual_3.png" alt="Gold Standard Generation Module" style="width:600px;height:400px"/>
</div>

<div>
<h4>Automatic Counting Module</h4>
<section>
<p lang="en">In the Automatic Counting Module, the selected palette is provided with an automatic counting solution by the application. In this module, it will be the responsibility of the algorithm provided by the tool to analyze the images and, based on the defined heuristics, identify and count the eggs in the palette frames.</p>
</section>
<img src="/images/mod_auto_3.png" alt="Auto Counting Module Image" style="width:600px;height:400px"/>
</div>

<div>
<h4>Evaluation Module</h4>
<section>
<p lang="en">To monitor the assertiveness of the automatic counting algorithm, it is necessary to automate a dedicated evaluator. This evaluator must use the manual module as a fundamental truth (Ground Truth), since it represents the actual location of the eggs found by a human. This is the responsibility of the Evaluation Module: to provide an algorithm that evaluates the assertiveness of the solution developed in the Automatic count based on the generated gold standard.</p>
</section>
<img src="/images/mod_result_3.png" alt="Assessment Module Image" style="width:600px;height:400px"/>
</div>
</section>
<br/> <br/>
<section>
<p lang="pt-BR">Projeto desenvolvido como parte do Trabalho de Conclusão de Curso da Instiruição IFPE campus recife do curso de TADS.</p>
<p lang="pt-BR">O objetivo geral deste trabalho é desenvolver uma aplicação monolítica de contagem e identificação de ovos do Aedes aegypti contidos em uma imagem, utilizando técnicas
de visão computacional.</p>

<p lang="pt-BR">O projeto possui os seguintes objetivos específicos:</p>
<ul>
<li>Desenvolver um módulo para a contagem dos ovos dos mosquitos Aedes pela ferramenta</li>
<li>Servir como parâmetro medidor da eficiência de políticas públicas de combate ao mosquito</li>
<li>Disponibilizar um banco de dados contendo as planilhas e as imagens resultantes do processamento</li>
</ul>

<p lang="pt-BR">Para utilizar este projeto será necessário instalar o OpenCV na localmente</p>
<h5>Instruções:</h5>
<ul>
<li>Acessar o <a href="https://opencv.org/releases/">site</a> do OpenCV na release necessária (4.7.0 neste projeto).</li>
<li>Baixar e instalar a opção correspondente ao seu sistema operacional (Windows neste projeto)</li>
<li>Após instalado, incluir a biblioteca do OpenCV no projeto neste caminho: (local de instalação)/opencv/build/java/opencv-470.jar</li>
<li>Alterar a propriedade "run.jvmargs" em project.properties para identificar o OpenCV instalado localmente: (caminho de instalação)/opencv/build/java/x64 </li>
<li>A propriedade anterior também pode ser alterada dentro do Netbeans seguindo o caminho: 
    <ul>
        <li>Clique no botão direito no projeto</li>
        <li>Acessar "Propriedades"</li>
        <li>Ao Abrir, Acessar "Executar"</li>
        <li>Abrindo a opção, deve-se alterar no campo VM Options para: -Djava.library.path="(caminho de instalação)opencv\build\java\x64" --enable-preview</li>
        <li>Obs: A biblioteca do OpenCV não está preparada para acentuação em portugês. Portanto, deve-se ter cuidado ao escolher onde salvar as imagens das paletas.</li>
    </ul>
</li>
</ul>

<h4>Estruturação dos Módulos da Aplicação</h4>
<p lang="pt-BR">O software completo é composto por três módulos:</p>
<ul>
<li>Módulo Geração do Padrão Ouro</li>
<li>Módulo de Contagem Automática</li>
<li>Módulo de Avaliação</li>
</ul>
 
<div>
<h4>Módulo Geração do Padrão Ouro</h4>
<section>
<p lang="pt-BR">No Módulo de Geração do Padrão Ouro, é fornecida uma ferramenta de contagem manual dos ovos. Neste módulo aplicação oferece uma ferramenta a qual permite que, para cada quadro que compõe uma paleta, deve ser demarcada manualmente na tela a área em que se localiza cada ovo individualmente. A quantidade de ovos será a quantidade de regiões demarcadas.</p>
</section>
<img src="/images/mod_manual_3.png" alt="Módulo Geração do Padrão Ouro" style="width:600px;height:400px"/>
</div>

<div>
<h4>Módulo de Contagem Automática</h4>
<section>
<p lang="pt-BR">No Módulo de Contagem Automática, à paleta selecionada é fornecida uma solução de contagem automática pela aplicação. Neste módulo, será de responsabilidade do algoritmo fornecido pela ferramenta analisar as imagens e, baseado nas heurísticas definidas, identificar e contar os ovos nos quadros da paleta.</p>
</section>
<img src="/images/mod_auto_3.png" alt="Imagem do Módulo de Contagem Automática" style="width:600px;height:400px"/>
</div>

<div>
<h4>Módulo de Avaliação</h4>
<section>
<p lang="pt-BR">Para monitorar a assertividade do algoritmo de contagem automática, se faz necessário automatizar um avaliador dedicado. Esse avaliador deve utilizar o módulo manual como verdade fundamental (Ground Truth), já que ele representa a localização real dos ovos encontrada por um humano. Esta é a responsabilidade do Módulo de Avaliação: fornecer um algoritmo que avalia a assertividade da solução desenvolvida na contagem Automática baseado no padrão ouro gerado.</p>
</section>
<img src="/images/mod_result_3.png" alt="Imagem do Módulo de Avaliação" style="width:600px;height:400px"/>
</div>
</section>
<br/> <br/>

<section>
<p lang="es">Proyecto desarrollado como parte del Trabajo de Finalización de Curso de la institución IFPE campus recife del curso TADS.</p>
<p lang="es">El objetivo general de este trabajo es desarrollar una aplicación monolítica para el conteo e identificación de huevos de Aedes aegypti contenidos en una imagen, utilizando técnicas
visión artificial.</p>

<p lang="es">El proyecto tiene los siguientes objetivos específicos:</p>
<ul>
<li>Desarrolle un módulo para contar huevos de mosquito Aedes usando la herramienta</li>
<li>Servir como parámetro de medición de la eficiencia de las políticas públicas para combatir el mosquito</li>
<li>Proporcionar una base de datos que contenga las hojas de trabajo y las imágenes resultantes del procesamiento</li>
</ul>

<p lang="es">Para usar este proyecto necesitarás instalar OpenCV localmente</p>
<h5>Instrucciones:</h5>
<ul>
<li>Acceda al <a href="https://opencv.org/releases/">sitio web de OpenCV</a> en la versión requerida (4.7.0 en este proyecto).</li>
<li>Descargue e instale la opción correspondiente a su sistema operativo (Windows en este proyecto)</li>
<li>Una vez instalada, incluya la biblioteca OpenCV en el proyecto en esta ruta: (ubicación de instalación)/opencv/build/java/opencv-470.jar</li>
<li>Cambie la propiedad "run.jvmargs" en project.properties para identificar OpenCV instalado localmente: (ruta de instalación)/opencv/build/java/x64 </li>
<li>La propiedad anterior también se puede cambiar dentro de Netbeans siguiendo la ruta:
     <ul>
         <li>Haga clic derecho en el proyecto</li>
         <li>Acceda a "Propiedades"</li>
         <li>Al abrir, acceda a "Ejecutar"</li>
         <li>Al abrir la opción, debe cambiar el campo Opciones de VM a: -Djava.library.path="(ruta de instalación)opencv\build\java\x64" --enable-preview</li>
         <li>Nota: La biblioteca OpenCV no está preparada para acentuaciónes. Por lo tanto, se debe tener cuidado al elegir dónde guardar las imágenes de la paleta.</li>
     </ul>
</li>
</ul>

<h4>Estructuración de los módulos de la aplicación</h4>
<p>El software completo consta de tres módulos:</p>
<ul>
<li>Módulo de Generación del Ground Truth</li>
<li>Módulo de conteo automático</li>
<li>Módulo de Evaluación</li>
</ul>
 
<div>
<h4>Módulo de Generación del Ground Truth</h4>
<sección>
<p lang="es">En el Módulo de Generación del Ground Truth, se proporciona una herramienta de conteo manual de huevos. En este módulo, la aplicación ofrece una herramienta que permite, para cada cuadro que compone una paleta, marcar manualmente en pantalla la zona donde se encuentra cada huevo individual. La cantidad de huevos será la cantidad de regiones demarcadas.</p>
</sección>
<img src="/images/mod_manual_3.png" alt="Módulo de Generación del Ground Truth" style="width:600px;height:400px"/>
</div>

<div>
<h4>Módulo de conteo automático</h4>
<sección>
<p lang="es">En el Módulo de conteo automático, la aplicación proporciona a la paleta seleccionada una solución de conteo automático. En este módulo será responsabilidad del algoritmo proporcionado por la herramienta analizar las imágenes y, en base a las heurísticas definidas, identificar y contar los huevos en los marcos de la paleta.</p>
</sección>
<img src="/images/mod_auto_3.png" alt="Imagen del módulo de conteo automático" style="width:600px;height:400px"/>
</div>

<div>
<h4>Módulo de Evaluación</h4>
<sección>
<p lang="es">Para controlar la asertividad del algoritmo de conteo automático, es necesario automatizar un evaluador dedicado. Este evaluador debe utilizar el módulo manual como verdad fundamental (Ground Truth), ya que representa la ubicación real de los huevos encontrados por un humano. Esta es responsabilidad del Módulo de Evaluación: proporcionar un algoritmo que evalúe la asertividad de la solución desarrollada en el Conteo Automático con base en el estándar oro generado.</p>
</sección>
<img src="/images/mod_result_3.png" alt="Imagen del módulo de evaluación" style="width:600px;height:400px"/>
</div>
</section>